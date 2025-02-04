package sri.sysint.sri_starter_back.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sri.sysint.sri_starter_back.model.Building;
import sri.sysint.sri_starter_back.model.DDeliverySchedule;
import sri.sysint.sri_starter_back.repository.DDeliveryScheduleRepo;

@Service
@Transactional
public class DDeliveryScheduleServiceImpl {
    @Autowired
    private DDeliveryScheduleRepo dDeliveryScheduleRepo;

    public DDeliveryScheduleServiceImpl(DDeliveryScheduleRepo dDeliveryScheduleRepo) {
        this.dDeliveryScheduleRepo = dDeliveryScheduleRepo;
    }

    public BigDecimal getNewId() {
        return dDeliveryScheduleRepo.getNewId().add(BigDecimal.valueOf(1));
    }

    public List<DDeliverySchedule> getAllDDeliverySchedule() {
        Iterable<DDeliverySchedule> dDeliverySchedules = dDeliveryScheduleRepo.getDataOrderId();
        List<DDeliverySchedule> dDeliveryScheduleList = new ArrayList<>();
        for (DDeliverySchedule item : dDeliverySchedules) {
            DDeliverySchedule dDeliveryScheduleTemp = new DDeliverySchedule(item);
            dDeliveryScheduleList.add(dDeliveryScheduleTemp);
        }

        return dDeliveryScheduleList;
    }

    public Optional<DDeliverySchedule> getDDeliveryScheduleById(BigDecimal id) {
        Optional<DDeliverySchedule> dDeliverySchedule = dDeliveryScheduleRepo.findById(id);
        return dDeliverySchedule;
    }

    public DDeliverySchedule saveDDeliverySchedule(DDeliverySchedule dDeliverySchedule) {
        try {
            dDeliverySchedule.setDETAIL_DS_ID(getNewId());
            dDeliverySchedule.setSTATUS(BigDecimal.valueOf(1));
            dDeliverySchedule.setCREATION_DATE(new Date());
            dDeliverySchedule.setLAST_UPDATE_DATE(new Date());
            return dDeliveryScheduleRepo.save(dDeliverySchedule);
        } catch (Exception e) {
            System.err.println("Error saving DDeliverySchedule: " + e.getMessage());
            throw e;
        }
    }

    public DDeliverySchedule updateDDeliverySchedule(DDeliverySchedule dDeliverySchedule) {
        try {
            Optional<DDeliverySchedule> currentDDeliveryScheduleOpt = dDeliveryScheduleRepo.findById(dDeliverySchedule.getDETAIL_DS_ID());

            if (currentDDeliveryScheduleOpt.isPresent()) {
                DDeliverySchedule currentDDeliverySchedule = currentDDeliveryScheduleOpt.get();

                currentDDeliverySchedule.setDS_ID(dDeliverySchedule.getDS_ID());
                currentDDeliverySchedule.setPART_NUM(dDeliverySchedule.getPART_NUM());
                currentDDeliverySchedule.setDATE_DS(dDeliverySchedule.getDATE_DS());
                currentDDeliverySchedule.setTOTAL_DELIVERY(dDeliverySchedule.getTOTAL_DELIVERY());
                currentDDeliverySchedule.setLAST_UPDATE_DATE(new Date());
                currentDDeliverySchedule.setLAST_UPDATED_BY(dDeliverySchedule.getLAST_UPDATED_BY());

                return dDeliveryScheduleRepo.save(currentDDeliverySchedule);
            } else {
                throw new RuntimeException("DDeliverySchedule with ID " + dDeliverySchedule.getDETAIL_DS_ID() + " not found.");
            }
        } catch (Exception e) {
            System.err.println("Error updating DDeliverySchedule: " + e.getMessage());
            throw e;
        }
    }

    public DDeliverySchedule deleteDDeliverySchedule(DDeliverySchedule dDeliverySchedule) {
        try {
            Optional<DDeliverySchedule> currentDDeliveryScheduleOpt = dDeliveryScheduleRepo.findById(dDeliverySchedule.getDETAIL_DS_ID());

            if (currentDDeliveryScheduleOpt.isPresent()) {
                DDeliverySchedule currentDDeliverySchedule = currentDDeliveryScheduleOpt.get();

                currentDDeliverySchedule.setSTATUS(BigDecimal.valueOf(0));
                currentDDeliverySchedule.setLAST_UPDATE_DATE(new Date());
                currentDDeliverySchedule.setLAST_UPDATED_BY(dDeliverySchedule.getLAST_UPDATED_BY());

                return dDeliveryScheduleRepo.save(currentDDeliverySchedule);
            } else {
                throw new RuntimeException("DDeliverySchedule with ID " + dDeliverySchedule.getDETAIL_DS_ID() + " not found.");
            }
        } catch (Exception e) {
            System.err.println("Error updating DDeliverySchedule: " + e.getMessage());
            throw e;
        }
    }

    public void deleteAllDDeliverySchedule() {
        dDeliveryScheduleRepo.deleteAll();
    }
    
    public DDeliverySchedule restoreDDeliverySchedule(DDeliverySchedule dDeliverySchedule) {
        try {
            Optional<DDeliverySchedule> currentDDeliveryScheduleOpt = dDeliveryScheduleRepo.findById(dDeliverySchedule.getDETAIL_DS_ID());

            if (currentDDeliveryScheduleOpt.isPresent()) {
                DDeliverySchedule currentDDeliverySchedule = currentDDeliveryScheduleOpt.get();

                currentDDeliverySchedule.setSTATUS(BigDecimal.valueOf(1));
                currentDDeliverySchedule.setLAST_UPDATE_DATE(new Date());
                currentDDeliverySchedule.setLAST_UPDATED_BY(dDeliverySchedule.getLAST_UPDATED_BY());

                return dDeliveryScheduleRepo.save(currentDDeliverySchedule);
            } else {
                throw new RuntimeException("DDeliverySchedule with ID " + dDeliverySchedule.getDETAIL_DS_ID() + " not found.");
            }
        } catch (Exception e) {
            System.err.println("Error updating DDeliverySchedule: " + e.getMessage());
            throw e;
        }
    }
    
    public ByteArrayInputStream exportDDeliverySchedulesExcel() throws IOException {
        List<DDeliverySchedule> dDeliverySchedules = dDeliveryScheduleRepo.getDataOrderId();
        ByteArrayInputStream byteArrayInputStream = dataToExcel(dDeliverySchedules);
        return byteArrayInputStream;
    }
    
    private ByteArrayInputStream dataToExcel(List<DDeliverySchedule> dDeliverySchedules) throws IOException {
        String[] header = {
            "DETAIL_DS_ID",
            "DELIVERYSCHEDULE_ID",
            "PART_NUM",
            "DATE",
            "TOTAL_DELIVERY"
        };

        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            Sheet sheet = workbook.createSheet("DDeliverySchedule Data");

            // Create font for header
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);

            // Create cell style with border
            CellStyle borderStyle = workbook.createCellStyle();
            borderStyle.setBorderTop(BorderStyle.THIN);
            borderStyle.setBorderBottom(BorderStyle.THIN);
            borderStyle.setBorderLeft(BorderStyle.THIN);
            borderStyle.setBorderRight(BorderStyle.THIN);
            borderStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
            borderStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            borderStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            borderStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());

            // Create header style with yellow background and border
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.cloneStyleFrom(borderStyle);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Set column widths
            for (int i = 0; i < header.length; i++) {
                sheet.setColumnWidth(i, 20 * 256); // 20 characters wide
            }

            // Create the header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < header.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(header[i]);
                cell.setCellStyle(headerStyle);
            }

            // Fill data rows
            int rowIndex = 1;
            for (DDeliverySchedule d : dDeliverySchedules) {
                Row dataRow = sheet.createRow(rowIndex++);

                // DETAIL_DS_ID
                Cell detailDsIdCell = dataRow.createCell(0);
                detailDsIdCell.setCellValue(d.getDETAIL_DS_ID().doubleValue());
                detailDsIdCell.setCellStyle(borderStyle);

                // DELIVERYSCHEDULE_ID
                Cell deliveryScheduleIdCell = dataRow.createCell(1);
                deliveryScheduleIdCell.setCellValue(d.getDS_ID() != null ? d.getDS_ID().doubleValue() : null);
                deliveryScheduleIdCell.setCellStyle(borderStyle);

                // PART_NUM
                Cell partNumCell = dataRow.createCell(2);
                partNumCell.setCellValue(d.getPART_NUM().doubleValue());                
                partNumCell.setCellStyle(borderStyle);
                // DATE
                Cell dateCell = dataRow.createCell(3);
                dateCell.setCellValue(d.getDATE_DS() != null ? d.getDATE_DS().toString() : "");
                dateCell.setCellStyle(borderStyle);

                // TOTAL_DELIVERY
                Cell totalDeliveryCell = dataRow.createCell(4);
                totalDeliveryCell.setCellValue(d.getTOTAL_DELIVERY().doubleValue());
                totalDeliveryCell.setCellStyle(borderStyle);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to export DDeliverySchedule data");
            throw e;
        } finally {
            workbook.close();
            out.close();
        }
    }
}
