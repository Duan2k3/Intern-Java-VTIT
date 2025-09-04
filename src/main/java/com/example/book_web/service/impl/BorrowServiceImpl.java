package com.example.book_web.service.impl;

import com.example.book_web.Exception.DataExistingException;
import com.example.book_web.Exception.DataNotFoundException;
import com.example.book_web.common.MessageCommon;
import com.example.book_web.dto.book.FilterBookDTO;
import com.example.book_web.dto.book.PageResponse;
import com.example.book_web.dto.borrow.BorrowDTO;
import com.example.book_web.dto.borrow.ReturnBookDTO;
import com.example.book_web.dto.borrow.InforBorrowDto;
import com.example.book_web.dto.borrow_detail.BorrowDetailDTO;
import com.example.book_web.entity.Book;
import com.example.book_web.entity.Borrow;
import com.example.book_web.entity.BorrowDetail;
import com.example.book_web.entity.User;
import com.example.book_web.enums.BorrowStatus;
import com.example.book_web.repository.BookRepository;
import com.example.book_web.repository.BorrowDetailRepository;
import com.example.book_web.repository.BorrowRepository;
import com.example.book_web.repository.UserRepository;
import com.example.book_web.repository.custom.BorrowCustomRepository;
import com.example.book_web.request.borrow.BorrowRequest;
import com.example.book_web.request.borrow.FilterBorrowRequest;
import com.example.book_web.request.borrow_detail.BorrowDetailRequest;
import com.example.book_web.request.borrow.ReturnBookRequest;
import com.example.book_web.service.BorrowService;
import com.example.book_web.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BorrowServiceImpl implements BorrowService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BorrowRepository borrowRepository;
    private final BorrowDetailRepository borrowDetailRepository;
    private final BookRepository bookRepository;
    private final JwtService jwtService;
    private final MessageCommon messageCommon;
    private final BorrowCustomRepository borrowCustomRepository;


    @Transactional
    @Override
    public BorrowDTO createBorrow(String token , BorrowRequest request)  {
        log.info("Creating borrow for user with token: {}", token);

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String name = jwtService.extractUsername(token);
        User existingUser = userRepository.findByUsername(name)
                .orElseThrow(() -> new DataNotFoundException("User not found", "400"));
        Borrow borrow = Borrow.builder()
                .borrowDate(LocalDate.now())
                .returnDate(request.getReturnDate())
                .user(existingUser)
                .note(request.getNote())
                .build();
        List<Long> bookIds = request.getBorrowDetails()
                .stream()
                .map(BorrowDetailRequest::getBookId)
                .toList();

        List<Book> books = bookRepository.findAllById(bookIds);

        Map<Long, Book> bookMap = books.stream()
                .collect(Collectors.toMap(Book::getId, b -> b));

        List<BorrowDetail> details = new ArrayList<>();
        for (BorrowDetailRequest d : request.getBorrowDetails()) {
            Book book = bookMap.get(d.getBookId());
            if (book == null) {
                throw new DataNotFoundException(messageCommon.getMessage(MessageKeys.BOOK.BOOK_NOT_EXIST),"400");
            }
            if (book.getQuantity() < d.getQuantity()) {
                throw new DataNotFoundException(messageCommon.getMessage(MessageKeys.BOOK.QUANTITY_NOT_VALID),"400");
            }
            book.setQuantity(book.getQuantity() - d.getQuantity());

            BorrowDetail detail = BorrowDetail.builder()
                    .book(book)
                    .quantity(d.getQuantity())
                    .status(BorrowStatus.BORROWING)
                    .borrow(borrow)
                    .build();
            details.add(detail);
        }

        borrow.setBorrowDetails(details);

        borrowRepository.save(borrow);
        log.info("Borrow created successfully with ID: {}", borrow.getId());

        BorrowDTO dto = BorrowDTO.builder()
                .userId(borrow.getUser().getId())
                .borrowDate(borrow.getBorrowDate())
                .returnDate(borrow.getReturnDate())
                .borrowDetails(
                        borrow.getBorrowDetails().stream()
                                .map(detail -> BorrowDetailDTO.builder()
                                        .bookId(detail.getBook().getId())
                                        .quantity(detail.getQuantity())
                                        .build()
                                )
                                .toList()
                )
                .build();

        return dto;
    }

    @Override
    @Transactional
    public BorrowDTO updateBorrow(ReturnBookDTO bookDTO) {
        log.info("Updating borrow with ID: {}", bookDTO.getId());
        Borrow borrow = borrowRepository.findById(bookDTO.getId())
                .orElseThrow(() -> new DataNotFoundException(
                        messageCommon.getMessage(MessageKeys.BORROW.BORROW_NOT_EXISTING) + bookDTO.getId(), "400"));
        List<BorrowDetail> details = borrowDetailRepository.findByBorrowIdIn(bookDTO.getBorrowDetailIds());
        if (details.isEmpty()) {
            throw new DataNotFoundException(
                    messageCommon.getMessage(MessageKeys.BORROW_HISTORY.TITLE_NOT_NULL), "400");
        }
        for (BorrowDetail detail : details) {
            detail.setActualReturnedDate(bookDTO.getActualReturnedDate());

            LocalDate expectedReturnDate = borrow.getReturnDate();
            LocalDate actualReturnDate = bookDTO.getActualReturnedDate();

            if (actualReturnDate != null) {
                if (actualReturnDate.isAfter(expectedReturnDate)) {
                    detail.setStatus(BorrowStatus.LATE_RETURN);
                } else {
                    detail.setStatus(BorrowStatus.RETURNED);
                }
            } else {
                if (LocalDate.now().isAfter(expectedReturnDate)) {
                    detail.setStatus(BorrowStatus.OVERDUE);
                } else {
                    detail.setStatus(BorrowStatus.NOT_RETURNED);
                }
            }
        }

        borrowDetailRepository.saveAll(details);

        log.info("Borrow updated successfully with ID: {}", borrow.getId());

        return BorrowDTO.builder()
                .userId(borrow.getUser().getId())
                .borrowDate(borrow.getBorrowDate())
                .returnDate(borrow.getReturnDate())
                .borrowDetails(
                        borrow.getBorrowDetails().stream()
                                .map(detail -> BorrowDetailDTO.builder()
                                        .bookId(detail.getBook().getId())
                                        .quantity(detail.getQuantity())
                                        .build()
                                )
                                .toList()
                )
                .build();
    }


    /**
     * @return
     */
    @Override
    public List<Borrow> getAllBorrows() {

        List<Borrow> borrows = borrowRepository.findAll();

        for (Borrow borrow : borrows) {
            for (BorrowDetail detail : borrow.getBorrowDetails()) {
                if (detail.getActualReturnedDate() == null && LocalDate.now().isAfter(borrow.getReturnDate())) {
                    detail.setStatus(BorrowStatus.OVERDUE);
                } else if (detail.getActualReturnedDate() == null) {
                    detail.setStatus(BorrowStatus.NOT_RETURNED);
                }
            }
        }
log.info("Updating borrow statuses if necessary");
        borrowRepository.saveAll(borrows);
        return borrows;
    }

    /**
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public Borrow getBorrow(Long id) {
        return borrowRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(messageCommon.getMessage(MessageKeys.BORROW.BORROW_NOT_EXISTING) + id, "404"));
    }

    /**
     * @param id
     * @throws Exception
     */
    @Override
    @Transactional
    public void deleteBorrow(Long id) {
        log.info("Deleting borrow with ID: {}", id);
        Borrow borrow = borrowRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(messageCommon.getMessage(MessageKeys.BORROW.BORROW_NOT_EXISTING) + id, "404"));
        borrowRepository.delete(borrow);
    }

    @Override
    public List<BorrowDetail> getBorrowHistory1(String token){
        log.info("Fetching borrow history for user with token: {}", token);
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String name = jwtService.extractUsername(token);
       Optional<User>  user = userRepository.findByUsername(name);
       User existingUser = user.get();
       Long id = existingUser.getId();


       List<BorrowDetail> list = borrowDetailRepository.getBorrowHistory(id);

       return list;

    }

    /**
     * @param request
     */
    @Override
    @Transactional
    public void returnBook(String token ,ReturnBookRequest request) {
        log.info("Processing return for borrow ID: {}", request.getBorrowId());

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String name = jwtService.extractUsername(token);
        User existingUser = userRepository.findByUsername(name)
                .orElseThrow(() -> new DataNotFoundException("User not found", "400"));

        Long id = existingUser.getId();

        Borrow borrow= borrowRepository.checkMatch(request.getBorrowId(), id);
        if (borrow == null) {
            throw new DataNotFoundException("Phieu muon cua nguoi dung hien tai khong ho le", "404");
        }

        List<BorrowDetail> details = borrowDetailRepository.findAllByBorrowIdAndBookIds(borrow.getId(), request.getBookIds());

        if (details.size() != request.getBookIds().size()) {
            throw new DataNotFoundException(messageCommon.getMessage(MessageKeys.BORROW.BORROW_NOT_EXISTING), "404");
        }

        for (BorrowDetail detail : details) {
            if (detail.getStatus() == BorrowStatus.RETURNED || detail.getStatus() == BorrowStatus.LATE_RETURN) {
                throw new DataExistingException(
                        "Sách đã được trả  " + detail.getBook().getId(), "400");
            }

            Book book = detail.getBook();
            book.setQuantity(book.getQuantity() + detail.getQuantity());

            detail.setActualReturnedDate(LocalDate.now());

            if (detail.getActualReturnedDate().isAfter(borrow.getReturnDate())) {
                detail.setStatus(BorrowStatus.LATE_RETURN);
            } else {
                detail.setStatus(BorrowStatus.RETURNED);
            }
        }

        borrowDetailRepository.saveAll(details);
    }


    /**
     * @param id
     * @return
     */
//    @Override
//    public List<InforBorrowDto> getInforBorrow(Long id) {
//        log.info("Fetching borrow history for user ID: {}", id);
//        List<InforBorrowDto> list = borrowRepository.getBorrowHistory(id);
//        return list;
//    }
//
//    /**
//     * @param token
//     * @return
//     */
//    @Override
//    public List<InforBorrowDto> getHistoryById(String token) {
//        if (token.startsWith("Bearer ")) {
//            token = token.substring(7);
//        }
//        String name = jwtService.extractUsername(token);
//        Optional<User>  user = userRepository.findByUsername(name);
//        User existingUser = user.get();
//        Long userId = existingUser.getId();
//        log.info("Fetching borrow history for user ID: {}", userId);
//        List<InforBorrowDto> list = borrowRepository.getBorrowHistory(userId);
//        return list;
//    }

    /**
     * @param request

     * @return
     */
    @Override
    public PageResponse<InforBorrowDto> getList(String token ,FilterBorrowRequest request) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String name = jwtService.extractUsername(token);
        Optional<User>  user = userRepository.findByUsername(name);
        User existingUser = user.get();
        Long userId = existingUser.getId();
        int pageNumber = request.getPageNumber() != null && request.getPageNumber() > 0
                ? request.getPageNumber() - 1
                : 0;
        int pageSize = request.getPageSize() != null ? request.getPageSize() : 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<InforBorrowDto> page = borrowCustomRepository.listBorrowHistory(userId,request, pageable);

        return new PageResponse<>(
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getContent()
        );
    }

}


