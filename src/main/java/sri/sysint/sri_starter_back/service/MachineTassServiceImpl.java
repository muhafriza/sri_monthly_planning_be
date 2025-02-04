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
import sri.sysint.sri_starter_back.model.MachineTass;
import sri.sysint.sri_starter_back.repository.MachineTassRepo;

@Service
@Transactional
public class MachineTassServiceImpl {
    @Autowired
    private MachineTassRepo machineTassRepo;

    public MachineTassServiceImpl(MachineTassRepo machineTassRepo) {
        this.machineTassRepo = machineTassRepo;
    }

    public List<MachineTass> getAllMachineTass() {
        Iterable<MachineTass> machineTasses = machineTassRepo.getDataOrderId();
        List<MachineTass> machineTassList = new ArrayList<>();
        for (MachineTass machine : machineTasses) {
            MachineTass machineTassTemp = new MachineTass(machine);
            machineTassList.add(machineTassTemp);
        }
        return machineTassList;
    }

    public Optional<MachineTass> getMachineTassById(String id) {
        Optional<MachineTass> machineTass = machineTassRepo.findById(id);
        return machineTass;
    }

    public MachineTass saveMachineTass(MachineTass machineTass) {
        try {
        	machineTass.setID_MACHINE_TASS(machineTass.getID_MACHINE_TASS());
            machineTass.setSTATUS(BigDecimal.valueOf(1));
            machineTass.setCREATION_DATE(new Date());
            machineTass.setLAST_UPDATE_DATE(new Date());
            return machineTassRepo.save(machineTass);
        } catch (Exception e) {
            System.err.println("Error saving machineTass: " + e.getMessage());
            throw e;
        }
    }

    public MachineTass updateMachineTass(MachineTass machineTass) {
        try {
            Optional<MachineTass> currentMachineTassOpt = machineTassRepo.findById(machineTass.getID_MACHINE_TASS());
            if (currentMachineTassOpt.isPresent()) {
                MachineTass currentMachineTass = currentMachineTassOpt.get();
                
                currentMachineTass.setBUILDING_ID(machineTass.getBUILDING_ID());
                currentMachineTass.setFLOOR(machineTass.getFLOOR());
                currentMachineTass.setMACHINE_NUMBER(machineTass.getMACHINE_NUMBER());
                currentMachineTass.setTYPE(machineTass.getTYPE());
                currentMachineTass.setWORK_CENTER_TEXT(machineTass.getWORK_CENTER_TEXT());
                
                currentMachineTass.setLAST_UPDATE_DATE(new Date());
                currentMachineTass.setLAST_UPDATED_BY(machineTass.getLAST_UPDATED_BY());
                return machineTassRepo.save(currentMachineTass);
            } else {
                throw new RuntimeException("MachineTass with ID " + machineTass.getID_MACHINE_TASS() + " not found.");
            }
        } catch (Exception e) {
            System.err.println("Error updating machineTass: " + e.getMessage());
            throw e;
        }
    }

    public MachineTass deleteMachineTass(MachineTass machineTass) {
        try {
            Optional<MachineTass> currentMachineTassOpt = machineTassRepo.findById(machineTass.getID_MACHINE_TASS());
            if (currentMachineTassOpt.isPresent()) {
                MachineTass currentMachineTass = currentMachineTassOpt.get();
                currentMachineTass.setSTATUS(BigDecimal.valueOf(0));
                currentMachineTass.setLAST_UPDATE_DATE(new Date());
                currentMachineTass.setLAST_UPDATED_BY(machineTass.getLAST_UPDATED_BY());
                return machineTassRepo.save(currentMachineTass);
            } else {
                throw new RuntimeException("MachineTass with ID " + machineTass.getID_MACHINE_TASS() + " not found.");
            }
        } catch (Exception e) {
            System.err.println("Error updating machineTass: " + e.getMessage());
            throw e;
        }
    }
    
    public MachineTass restoreMachineTass(MachineTass machineTass) {
        try {
            Optional<MachineTass> currentMachineTassOpt = machineTassRepo.findById(machineTass.getID_MACHINE_TASS());
            
            if (currentMachineTassOpt.isPresent()) {
                MachineTass currentMachineTass = currentMachineTassOpt.get();
                
                currentMachineTass.setSTATUS(BigDecimal.valueOf(1)); 
                currentMachineTass.setLAST_UPDATE_DATE(new Date());
                currentMachineTass.setLAST_UPDATED_BY(machineTass.getLAST_UPDATED_BY());
                
                return machineTassRepo.save(currentMachineTass);
            } else {
                throw new RuntimeException("MachineTass with ID " + machineTass.getID_MACHINE_TASS() + " not found.");
            }
        } catch (Exception e) {
            System.err.println("Error restoring machineTass: " + e.getMessage());
            throw e;
        }
    }


    public void deleteAllMachineTass() {
        machineTassRepo.deleteAll();
    }
    
    public ByteArrayInputStream exportMachineTassExcel() throws IOException {
        List<MachineTass> machineTasss = machineTassRepo.getDataOrderId();
        ByteArrayInputStream byteArrayInputStream = dataToExcel(machineTasss);
        return byteArrayInputStream;
    }
    
    private ByteArrayInputStream dataToExcel(List<MachineTass> machineTasses) throws IOException {
        String[] header = {
            "NOMOR",
            "ID_MACHINE_TASS",
            "BUILDING_ID",
            "FLOOR",
            "MACHINE_NUMBER",
            "TYPE",
            "WORK_CENTER_TEXT"
        };

        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            Sheet sheet = workbook.createSheet("MACHINE TASS DATA");

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);

            CellStyle borderStyle = workbook.createCellStyle();
            borderStyle.setBorderTop(BorderStyle.THIN);
            borderStyle.setBorderBottom(BorderStyle.THIN);
            borderStyle.setBorderLeft(BorderStyle.THIN);
            borderStyle.setBorderRight(BorderStyle.THIN);
            borderStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
            borderStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            borderStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            borderStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.cloneStyleFrom(borderStyle);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            for (int i = 0; i < header.length; i++) {
                sheet.setColumnWidth(i, 20 * 256);
            }

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < header.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(header[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIndex = 1;
            int nomor = 1;
            for (MachineTass m : machineTasses) {
                Row dataRow = sheet.createRow(rowIndex++);

                Cell nomorCell = dataRow.createCell(0);
                nomorCell.setCellValue(nomor++);
                nomorCell.setCellStyle(borderStyle);

                Cell idCell = dataRow.createCell(1);
                idCell.setCellValue(m.getID_MACHINE_TASS());
                idCell.setCellStyle(borderStyle);

                Cell buildingIdCell = dataRow.createCell(2);
                buildingIdCell.setCellValue(m.getBUILDING_ID() != null ? m.getBUILDING_ID().doubleValue() : null);
                buildingIdCell.setCellStyle(borderStyle);

                Cell floorCell = dataRow.createCell(3);
                floorCell.setCellValue(m.getFLOOR() != null ? m.getFLOOR().doubleValue() : null);
                floorCell.setCellStyle(borderStyle);

                Cell machineNumberCell = dataRow.createCell(4);
                machineNumberCell.setCellValue(m.getMACHINE_NUMBER() != null ? m.getMACHINE_NUMBER().doubleValue() : null);
                machineNumberCell.setCellStyle(borderStyle);

                Cell typeCell = dataRow.createCell(5);
                typeCell.setCellValue(m.getTYPE() != null ? m.getTYPE() : "");
                typeCell.setCellStyle(borderStyle);

                Cell workCenterTextCell = dataRow.createCell(6);
                workCenterTextCell.setCellValue(m.getWORK_CENTER_TEXT() != null ? m.getWORK_CENTER_TEXT() : "");
                workCenterTextCell.setCellStyle(borderStyle);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Gagal mengekspor data MachineTass");
            throw e;
        } finally {
            workbook.close();
            out.close();
        }
    }

}
