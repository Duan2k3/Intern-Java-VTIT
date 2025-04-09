package com.example.book_web.service.impl;

import com.example.book_web.Exception.DataNotFoundException;
import com.example.book_web.dto.BorrowDTO;
import com.example.book_web.dto.BorrowDetailDTO;
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
import com.example.book_web.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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

    @Override
    public Borrow createBorrow(BorrowDTO borrowDTO) throws Exception {
        User user = userRepository.findById(borrowDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Borrow borrow = Borrow.builder()
                .borrowDate(LocalDate.now())
                .returnDate(borrowDTO.getReturnDate())
                .user(user)

                .build();

        List<BorrowDetail> details = new ArrayList<>();
        for (BorrowDetailDTO d : borrowDTO.getBorrowDetails()) {
            Book book = bookRepository.findById(d.getBookId())
                    .orElseThrow(() -> new RuntimeException("Book not found"));
            BorrowDetail detail = BorrowDetail.builder()
                    .book(book)
                    .quantity(d.getQuantity())
                    .status(BorrowStatus.BORROWING)
                    .borrow(borrow)
                    .build();
            details.add(detail);
        }

        borrow.setBorrowDetails(details);

        return borrowRepository.save(borrow);
    }


    @Override
    public Borrow updateBorrow(Long id, ReturnBookDTO bookDTO) throws Exception {
        List<BorrowDetail> details = borrowDetailRepository.findByBorrowIdIn(bookDTO.getBorrowDetailIds());

        if (details.isEmpty()) {
            throw new RuntimeException("No borrow details found for the provided ids.");
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
        Optional<Borrow> borrowOptional = borrowRepository.findById(id);
        if (borrowOptional.isPresent()) {
            return borrowOptional.get();
        } else {
            throw new RuntimeException("Borrow not found with id: " + id);
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
    public Borrow getBorrow(Long id) throws Exception {
        return borrowRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrow not found"));
    }

    /**
     * @param id
     * @throws Exception
     */
    @Override
    public void deleteBorrow(Long id) throws Exception {
        Borrow borrow = borrowRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrow not found"));
        borrowRepository.delete(borrow);
    }

    }


