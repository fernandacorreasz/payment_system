package com.payment_system.application.service;

import com.payment_system.application.exception.ResourceNotFoundException;
import com.payment_system.domain.model.Bill.Bill;
import com.payment_system.domain.model.Bill.dto.BillDTO;
import com.payment_system.domain.repository.BillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class BillServiceTest {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillService billService;

    @Mock
    private BillRepository mockBillRepository;

    @InjectMocks
    private BillService mockBillService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should save bill successfully")
    void testSaveBill() {
        // Test to verify that a bill is saved correctly and all attributes are properly set.
        Bill bill = new Bill();
        bill.setDueDate(new Date());
        bill.setPaymentDate(new Date());
        bill.setAmount(100.0);
        bill.setDescription("Test Bill 02");
        bill.setStatus("Pending");

        BillDTO savedBill = billService.saveBill(bill);

        assertNotNull(savedBill);
        assertEquals(bill.getDescription(), savedBill.getDescription());
        assertEquals(bill.getAmount(), savedBill.getAmount());
        assertEquals(bill.getDueDate(), savedBill.getDueDate());
        assertEquals(bill.getPaymentDate(), savedBill.getPaymentDate());
        assertEquals(bill.getStatus(), savedBill.getStatus());

        Optional<Bill> foundBill = billRepository.findById(savedBill.getId());
        assertTrue(foundBill.isPresent());
        assertEquals("Test Bill 02", foundBill.get().getDescription());
    }

    @Test
    @DisplayName("Should throw Exception when Bill is not found")
    void testGetBillByIdNotFound() {
        // Test to verify that an exception is thrown when a bill is not found by its ID.
        UUID id = UUID.randomUUID();

        when(mockBillRepository.findById(id)).thenReturn(Optional.empty());

        Exception thrown = assertThrows(ResourceNotFoundException.class, () -> {
            mockBillService.getBillById(id);
        });

        assertEquals("Bill with id " + id + " not found.", thrown.getMessage());
    }
    
    @Test
    @DisplayName("Should retrieve bills successfully")
    void testGetBills() {
         // Test to verify that bills are retrieved successfully and the result is not empty.
        Bill bill = new Bill();
        
        bill.setId(UUID.randomUUID());
        Page<Bill> page = new PageImpl<>(Collections.singletonList(bill));

        when(mockBillRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<BillDTO> result = billService.getBills(Pageable.unpaged());

        assertNotNull(result);
        List<BillDTO> billDTOList = result.getContent();
        assertNotNull(billDTOList);
        assertFalse(billDTOList.isEmpty()); 
    }

    @Test
    @DisplayName("Should retrieve total paid amount by period successfully")
    void testGetTotalPaidAmountByPeriod() {
        // Test to verify that the total paid amount is retrieved correctly for a given period.
        Date startDate = new Date();
        Date endDate = new Date();
        Double totalPaidAmount = 0.0;

        when(mockBillRepository.findTotalPaidAmountByPeriod(eq(startDate), eq(endDate))).thenReturn(totalPaidAmount);

        Double result = billService.getTotalPaidAmountByPeriod(startDate, endDate);

        assertNotNull(result);
        assertEquals(totalPaidAmount, result);
    }

    @Test
    @DisplayName("Should throw Exception when deleting non-existing Bill")
    void testDeleteBillNotFound() {
         // Test to verify that an exception is thrown when attempting to delete a non-existing bill.
        UUID id = UUID.randomUUID();

        when(mockBillRepository.existsById(id)).thenReturn(false);

        Exception thrown = assertThrows(ResourceNotFoundException.class, () -> {
            billService.deleteBill(id);
        });

        assertEquals("Bill with id " + id + " not found.", thrown.getMessage());
    }

    @Test
    @DisplayName("Should delete bill successfully")
    void testDeleteBill() {
         // Test to verify that a bill is deleted successfully when it exists.
        UUID id = UUID.fromString("b88d04f3-ffd2-4f4d-8af3-0ae1365e6120");

        when(mockBillRepository.existsById(id)).thenReturn(true);

        mockBillService.deleteBill(id);

        verify(mockBillRepository, times(1)).deleteById(id);
    }
}
