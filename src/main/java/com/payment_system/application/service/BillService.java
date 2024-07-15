package com.payment_system.application.service;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.payment_system.application.exception.ResourceNotFoundException;
import com.payment_system.domain.model.Bill.Bill;
import com.payment_system.domain.model.Bill.dto.BillDTO;
import com.payment_system.domain.model.Bill.dto.BillStatusDTO;
import com.payment_system.domain.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public BillDTO saveBill(Bill bill) {
        Bill savedBill = billRepository.save(bill);
        return convertToDTO(savedBill);
    }

     public List<Map<String, Object>> importBills(MultipartFile file) throws Exception {
        List<Bill> bills = new ArrayList<>();
        List<Map<String, Object>> importedBills = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            reader.mark(1);
            if (reader.read() != 0xFEFF) {
                reader.reset();
            }
            char delimiter = detectDelimiter(file);

            CSVParser parser = new CSVParserBuilder().withSeparator(delimiter).build();
            CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).build();

            String[] values;
            boolean isFirstLine = true;
            while ((values = csvReader.readNext()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                if (values.length != 5) {
                    throw new Exception("Invalid CSV format: each row must have 5 columns.");
                }

                Bill bill = new Bill();
                bill.setDueDate(parseDate(values[0]));
                bill.setPaymentDate(parseDate(values[1]));
                bill.setAmount(Double.parseDouble(values[2]));
                bill.setDescription(values[3]);
                bill.setStatus(values[4]);
                bills.add(bill);

                Map<String, Object> billInfo = new HashMap<>();
                billInfo.put("description", bill.getDescription());
                importedBills.add(billInfo);
            }
        } catch (Exception e) {
            throw new Exception("Error processing CSV file: " + e.getMessage());
        }

        billRepository.saveAll(bills);
        return importedBills;
    }

    private char detectDelimiter(MultipartFile file) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            reader.mark(1000);
            String line = reader.readLine();
            reader.reset();

            if (line.contains(";")) {
                return ';';
            } else if (line.contains(",")) {
                return ',';
            } else {
                throw new Exception("Unknown delimiter in CSV file");
            }
        }
    }

    private Date parseDate(String date) throws Exception {
        try {
            return dateFormat.parse(date);
        } catch (Exception e) {
            throw new Exception("Invalid date format: " + date);
        }
    }
    public Bill getBillById(UUID id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill with id " + id + " not found."));
    }
    
    public Page<BillDTO> getBills(Pageable pageable) {
        return billRepository.findAll(pageable).map(this::convertToDTO);
    }

    public Page<BillDTO> getBillsWithFilters(Date dueDate, String description, Pageable pageable) {
        if (dueDate != null && description != null) {
            return billRepository.findByDueDateAndDescriptionContaining(dueDate, description, pageable).map(this::convertToDTO);
        } else if (dueDate != null) {
            return billRepository.findByDueDate(dueDate, pageable).map(this::convertToDTO);
        } else if (description != null) {
            return billRepository.findByDescriptionContaining(description, pageable).map(this::convertToDTO);
        } else {
            return billRepository.findAll(pageable).map(this::convertToDTO);
        }
    }

    public Double getTotalPaidAmountByPeriod(Date startDate, Date endDate) {
        Double totalPaidAmount = billRepository.findTotalPaidAmountByPeriod(startDate, endDate);
        if (totalPaidAmount == null) {
            totalPaidAmount = 0.0;
        }
        return totalPaidAmount;
    }

    public void deleteBill(UUID id) {
        if (!billRepository.existsById(id)) {
            throw new ResourceNotFoundException("Bill with id " + id + " not found.");
        }
        billRepository.deleteById(id);
    }

    public Bill updateBill(UUID id, Bill bill) {
        if (!billRepository.existsById(id)) {
            throw new ResourceNotFoundException("Bill with id " + id + " not found.");
        }
        bill.setId(id);
        return billRepository.save(bill);
    }

    public BillStatusDTO updateBillStatus(UUID id, String status) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill with id " + id + " not found."));
        bill.setStatus(status);
        billRepository.save(bill);
        return new BillStatusDTO(bill.getId(), bill.getStatus(), "Status updated successfully.");
    }

    private BillDTO convertToDTO(Bill bill) {
        BillDTO billDTO = new BillDTO();
        billDTO.setId(bill.getId());
        billDTO.setDueDate(bill.getDueDate());
        billDTO.setPaymentDate(bill.getPaymentDate());
        billDTO.setAmount(bill.getAmount());
        billDTO.setDescription(bill.getDescription());
        billDTO.setStatus(bill.getStatus());
        return billDTO;
    }
}
