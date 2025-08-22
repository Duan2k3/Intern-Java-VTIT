package com.example.book_web.service.impl;

import com.example.book_web.Exception.DataExistingException;
import com.example.book_web.Exception.DataNotFoundException;
import com.example.book_web.common.MessageCommon;
import com.example.book_web.dto.BorrowDTO;
import com.example.book_web.dto.ReturnBookDTO;
import com.example.book_web.entity.Book;
import com.example.book_web.entity.Borrow;
import com.example.book_web.entity.BorrowDetail;
import com.example.book_web.entity.User;
import com.example.book_web.enums.BorrowStatus;
import com.example.book_web.repository.BookRepository;
import com.example.book_web.repository.BorrowDetailRepository;
import com.example.book_web.repository.BorrowRepository;
import com.example.book_web.repository.UserRepository;
import com.example.book_web.request.borrow.BorrowRequest;
import com.example.book_web.request.borrow_detail.BorrowDetailRequest;
import com.example.book_web.request.borrow.ReturnBookRequest;
import com.example.book_web.service.BorrowService;
import com.example.book_web.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BorrowServiceImpl implements BorrowService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BorrowRepository borrowRepository;
    private final BorrowDetailRepository borrowDetailRepository;
    private final BookRepository bookRepository;
    private final JwtService jwtService;
    private final MessageCommon messageCommon;

    @Transactional
    @Override
    public BorrowDTO createBorrow(String token , BorrowRequest request)  {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String name = jwtService.extractUsername(token);
        Optional<User>  user = userRepository.findByUsername(name);
        User existingUser = user.get();
        Long id = existingUser.getId();

        Borrow borrow = Borrow.builder()
                .borrowDate(LocalDate.now())
                .returnDate(request.getReturnDate())
                .user(existingUser)

                .build();

        List<BorrowDetail> details = new ArrayList<>();
        for (BorrowDetailRequest d : request.getBorrowDetails()) {
            Book book = bookRepository.findById(d.getBookId())
                    .orElseThrow(() -> new DataNotFoundException(messageCommon.getMessage(MessageKeys.BOOK.BOOK_NOT_EXIST),"400"));

            if(book.getQuantity() < d.getQuantity()){
                throw  new DataNotFoundException("Số lượng sách không đủ","400");
            }
            else {
                book.setQuantity(book.getQuantity()-d.getQuantity());
            }
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
        return modelMapper.map(request,BorrowDTO.class);
    }


    @Override
    public Borrow updateBorrow(ReturnBookDTO bookDTO)  {
        List<BorrowDetail> details = borrowDetailRepository.findByBorrowIdIn(bookDTO.getBorrowDetailIds());

        if (details.isEmpty()) {
            throw new DataNotFoundException(messageCommon.getMessage(MessageKeys.BORROW_HISTORY.TITLE_NOT_NULL),"400");
        }

        for (BorrowDetail detail : details) {
            detail.setActualReturnedDate(bookDTO.getActualReturnedDate());

            LocalDate expectedReturnDate = detail.getBorrow().getReturnDate();
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
        Optional<Borrow> borrowOptional = borrowRepository.findById(bookDTO.getId());
        if (borrowOptional.isPresent()) {
            return borrowOptional.get();
        } else {
            throw new DataNotFoundException(messageCommon.getMessage(MessageKeys.BORROW.BORROW_NOT_EXISTING) + bookDTO.getId(),"400");
        }
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
                .orElseThrow(() -> new DataNotFoundException("Borrow not found with id: " + id, "404"));
    }

    /**
     * @param id
     * @throws Exception
     */
    @Override
    public void deleteBorrow(Long id) {
        Borrow borrow = borrowRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Borrow not found with id: " + id, "404"));
        borrowRepository.delete(borrow);
    }

    @Override
    public List<BorrowDetail> getBorrowHistory1(String token){
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
    public void returnBook(String token ,ReturnBookRequest request) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String name = jwtService.extractUsername(token);
        Optional<User>  user = userRepository.findByUsername(name);
        User existingUser = user.get();
        Long id = existingUser.getId();

        Borrow borrow = borrowRepository.findById(request.getBorrowId())
                .orElseThrow(() -> new DataNotFoundException("Borrow not found with id: " + request.getBorrowId(), "404"));

        for (Long bookId : request.getBookIds()) {
            BorrowDetail detail = borrowDetailRepository.findByBorrowIdAndBookId(borrow.getId(), bookId)
                    .orElseThrow(() -> new DataNotFoundException("BorrowDetail not found for borrowId: " + borrow.getId() + " and bookId: " + bookId, "404"));

            if (detail.getStatus() == BorrowStatus.RETURNED || detail.getStatus() == BorrowStatus.LATE_RETURN) {
                throw new DataExistingException("Book with ID " + bookId + " has already been returned or is late.", "400");
            }

            Optional<Book> book = bookRepository.findById(bookId);
            if (book.isEmpty()) {
                throw new DataNotFoundException("Book not found with id: " + bookId, "404");
            }
            Book existingBook = book.get();
            existingBook.setQuantity(existingBook.getQuantity() + detail.getQuantity());

            detail.setActualReturnedDate(LocalDate.now());

            if (detail.getActualReturnedDate().isAfter(borrow.getReturnDate())) {
                detail.setStatus(BorrowStatus.LATE_RETURN);
            } else {
                detail.setStatus(BorrowStatus.RETURNED);
            }

            borrowDetailRepository.save(detail);
        }
    }


}


