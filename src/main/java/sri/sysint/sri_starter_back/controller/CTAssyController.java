package sri.sysint.sri_starter_back.controller;

import static sri.sysint.sri_starter_back.security.SecurityConstants.SECRET;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import sri.sysint.sri_starter_back.exception.ResourceNotFoundException;
import sri.sysint.sri_starter_back.model.CTAssy;
import sri.sysint.sri_starter_back.model.Plant;
import sri.sysint.sri_starter_back.model.Response;
import sri.sysint.sri_starter_back.service.CTAssyServiceImpl;

@CrossOrigin(maxAge = 3600)
@RestController
public class CTAssyController {
	
	private Response response;	

	@Autowired
	private CTAssyServiceImpl ctAssyServiceImpl;
	
	@PersistenceContext	
	private EntityManager em;
	
	@GetMapping("/getAllCTAssy")
	public Response getAllCTAssy(final HttpServletRequest req) throws ResourceNotFoundException {
		String header = req.getHeader("Authorization");

	    if (header == null || !header.startsWith("Bearer ")) {
	        throw new ResourceNotFoundException("JWT token not found or maybe not valid");
	    }

	    String token = header.replace("Bearer ", "");

	    try {
	        String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
	            .build()
	            .verify(token)
	            .getSubject();

	        if (user != null) {
	        	List<CTAssy> ctAssys = new ArrayList<>();
	        	ctAssys = ctAssyServiceImpl.getAllCTAssy();
		
			    response = new Response(
			        new Date(),
			        HttpStatus.OK.value(),
			        null,
			        HttpStatus.OK.getReasonPhrase(),
			        req.getRequestURI(),
			        ctAssys
			    );
	        } else {
	            throw new ResourceNotFoundException("User not found");
	        }
	    } catch (Exception e) {
	        throw new ResourceNotFoundException("JWT token is not valid or expired");
	    }

	    return response;
	}
	
	@GetMapping("/getCTAssyById/{id}")
	public Response getCTAssyById(final HttpServletRequest req, @PathVariable BigDecimal id) throws ResourceNotFoundException {
	    String header = req.getHeader("Authorization");

	    if (header == null || !header.startsWith("Bearer ")) {
	        throw new ResourceNotFoundException("JWT token not found or maybe not valid");
	    }

	    String token = header.replace("Bearer ", "");

	    try {
	        String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
	            .build()
	            .verify(token)
	            .getSubject();

	        if (user != null) {
	        	Optional<CTAssy> ctAssy = Optional.of(new CTAssy());
	        	ctAssy = ctAssyServiceImpl.getCTAssyById(id);

	    	    response = new Response(
	    	        new Date(),
	    	        HttpStatus.OK.value(),
	    	        null,
	    	        HttpStatus.OK.getReasonPhrase(),
	    	        req.getRequestURI(),
	    	        ctAssy
	    	    );
	        } else {
	            throw new ResourceNotFoundException("User not found");
	        }
	    } catch (Exception e) {
	        throw new ResourceNotFoundException("JWT token is not valid or expired");
	    }

	    return response;
	}
	
	@PostMapping("/saveCTAssy")
	public Response saveCTAssy(final HttpServletRequest req, @RequestBody CTAssy ctAssy) throws ResourceNotFoundException {
	    String header = req.getHeader("Authorization");

	    if (header == null || !header.startsWith("Bearer ")) {
	        throw new ResourceNotFoundException("JWT token not found or maybe not valid");
	    }

	    String token = header.replace("Bearer ", "");

	    try {
	        String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
	            .build()
	            .verify(token)
	            .getSubject();

	        if (user != null) {
	        	CTAssy savedCTAssy = ctAssyServiceImpl.saveCTAssy(ctAssy);

	    	    response = new Response(
	    	        new Date(),
	    	        HttpStatus.OK.value(),
	    	        null,
	    	        HttpStatus.OK.getReasonPhrase(),
	    	        req.getRequestURI(),
	    	        savedCTAssy
	    	    );
	        } else {
	            throw new ResourceNotFoundException("User not found");
	        }
	    } catch (Exception e) {
	        throw new ResourceNotFoundException("JWT token is not valid or expired");
	    }

	    return response;
	}
	
	@PostMapping("/updateCTAssy")
	public Response updateCTAssy(final HttpServletRequest req, @RequestBody CTAssy ctAssy) throws ResourceNotFoundException {
	    String header = req.getHeader("Authorization");

	    if (header == null || !header.startsWith("Bearer ")) {
	        throw new ResourceNotFoundException("JWT token not found or maybe not valid");
	    }

	    String token = header.replace("Bearer ", "");

	    try {
	        String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
	            .build()
	            .verify(token)
	            .getSubject();

	        if (user != null) {
	        	CTAssy updatedCTAssy = ctAssyServiceImpl.updateCTAssy(ctAssy);

	    	    response = new Response(
	    	        new Date(),
	    	        HttpStatus.OK.value(),
	    	        null,
	    	        HttpStatus.OK.getReasonPhrase(),
	    	        req.getRequestURI(),
	    	        updatedCTAssy
	    	    );
	        } else {
	            throw new ResourceNotFoundException("User not found");
	        }
	    } catch (Exception e) {
	        throw new ResourceNotFoundException("JWT token is not valid or expired");
	    }

	    return response;
	}
	
	@PostMapping("/deleteCTAssy")
	public Response deleteCTAssy(final HttpServletRequest req, @RequestBody CTAssy ctAssy) throws ResourceNotFoundException {
	    String header = req.getHeader("Authorization");

	    if (header == null || !header.startsWith("Bearer ")) {
	        throw new ResourceNotFoundException("JWT token not found or maybe not valid");
	    }

	    String token = header.replace("Bearer ", "");

	    try {
	        String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
	            .build()
	            .verify(token)
	            .getSubject();

	        if (user != null) {
	        	CTAssy deletedCTAssy = ctAssyServiceImpl.deleteCTAssy(ctAssy);

	    	    response = new Response(
	    	        new Date(),
	    	        HttpStatus.OK.value(),
	    	        null,
	    	        HttpStatus.OK.getReasonPhrase(),
	    	        req.getRequestURI(),
	    	        deletedCTAssy
	    	    );
	        } else {
	            throw new ResourceNotFoundException("User not found");
	        }
	    } catch (Exception e) {
	        throw new ResourceNotFoundException("JWT token is not valid or expired");
	    }

	    return response;
	}
	
	@PostMapping("/restoreCTAssy")
	public Response activateCTAssy(final HttpServletRequest req, @RequestBody CTAssy ctAssy) throws ResourceNotFoundException {
	    String header = req.getHeader("Authorization");

	    if (header == null || !header.startsWith("Bearer ")) {
	        throw new ResourceNotFoundException("JWT token not found or maybe not valid");
	    }

	    String token = header.replace("Bearer ", "");

	    try {
	        String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
	            .build()
	            .verify(token)
	            .getSubject();

	        if (user != null) {
	        	CTAssy activatedCTAssy = ctAssyServiceImpl.activateCTAssy(ctAssy);

	    	    response = new Response(
	    	        new Date(),
	    	        HttpStatus.OK.value(),
	    	        null,
	    	        HttpStatus.OK.getReasonPhrase(),
	    	        req.getRequestURI(),
	    	        activatedCTAssy
	    	    );
	        } else {
	            throw new ResourceNotFoundException("User not found");
	        }
	    } catch (Exception e) {
	        throw new ResourceNotFoundException("JWT token is not valid or expired");
	    }

	    return response;
	}
	
	@PostMapping("/saveCTAssyExcel")
	public Response saveCTAssyExcelFile(@RequestParam("file") MultipartFile file, final HttpServletRequest req) throws ResourceNotFoundException {
//		String header = req.getHeader("Authorization");
//
//	    if (header == null || !header.startsWith("Bearer ")) {
//	        throw new ResourceNotFoundException("JWT token not found or maybe not valid");
//	    }
//
//	    String token = header.replace("Bearer ", "");
//
//	    try {
//	        String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
//	            .build()
//	            .verify(token)
//	            .getSubject();
//
//	        if (user != null) {
	        	if (file.isEmpty()) {
	    	        return new Response(new Date(), HttpStatus.BAD_REQUEST.value(), null, "No file uploaded", req.getRequestURI(), null);
	    	    }
	    	    
	    	    ctAssyServiceImpl.deleteAllCTAssy();

	    	    try (InputStream inputStream = file.getInputStream()) {
	    	        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
	    	        XSSFSheet sheet = workbook.getSheetAt(0);
	    	        
	    	        List<CTAssy> ctAssys = new ArrayList<>();
	    	        
	    	        for (int i = 3; i <= sheet.getLastRowNum(); i++) {
	    	            Row row = sheet.getRow(i);
	    	            if (row != null) {
	    	            	
	    	            	boolean isEmptyRow = true;

	                        for (int j = 0; j < row.getLastCellNum(); j++) {
	                            Cell cell = row.getCell(j);
	                            if (cell != null && cell.getCellType() != CellType.BLANK) {
	                                isEmptyRow = false;
	                                break;
	                            }
	                        }
	                        
	                        if (isEmptyRow) {
	                            continue; 
	                        }
	                        
	    	                CTAssy ctAssy = new CTAssy();

	    	                Cell nameCell = row.getCell(1);

	    	                if (nameCell != null) {
	    	                    ctAssy.setCT_ASSY_ID(ctAssyServiceImpl.getNewId());  
	    	                    ctAssy.setWIP(getStringFromCell(row.getCell(0)));  
	    	                    ctAssy.setDESCRIPTION(getStringFromCell(row.getCell(1)));  
	    	                    ctAssy.setGROUP_COUNTER(getStringFromCell(row.getCell(2))); 
	    	                    ctAssy.setVAR_GROUP_COUNTER(getStringFromCell(row.getCell(3)));  
	    	                    ctAssy.setSEQUENCE(getBigDecimalFromCell(row.getCell(4)));  
	    	                    ctAssy.setWCT(getStringFromCell(row.getCell(5)));  
	    	                    ctAssy.setOPERATION_SHORT_TEXT(getStringFromCell(row.getCell(6))); 
	    	                    ctAssy.setOPERATION_UNIT(getStringFromCell(row.getCell(7))); 
	    	                    ctAssy.setBASE_QUANTITY(getBigDecimalFromCell(row.getCell(8))); 
	    	                    ctAssy.setSTANDARD_VALUE_UNIT(getStringFromCell(row.getCell(9)));  
	    	                    ctAssy.setCT_SEC_1(getBigDecimalFromCell(row.getCell(10))); 
	    	                    ctAssy.setCT_HR_1000(getBigDecimalFromCell(row.getCell(11)));  
	    	                    ctAssy.setWH_NORMAL_SHIFT_0(getBigDecimalFromCell(row.getCell(12))); 
	    	                    ctAssy.setWH_NORMAL_SHIFT_1(getBigDecimalFromCell(row.getCell(13))); 
	    	                    ctAssy.setWH_NORMAL_SHIFT_2(getBigDecimalFromCell(row.getCell(14))); 
	    	                    ctAssy.setWH_SHIFT_FRIDAY(getBigDecimalFromCell(row.getCell(15))); 
	    	                    ctAssy.setWH_TOTAL_NORMAL_SHIFT(getBigDecimalFromCell(row.getCell(16)));  
	    	                    ctAssy.setWH_TOTAL_SHIFT_FRIDAY(getBigDecimalFromCell(row.getCell(17)));  
	    	                    ctAssy.setALLOW_NORMAL_SHIFT_0(getBigDecimalFromCell(row.getCell(18)));  
	    	                    ctAssy.setALLOW_NORMAL_SHIFT_1(getBigDecimalFromCell(row.getCell(19)));  
	    	                    ctAssy.setALLOW_NORMAL_SHIFT_2(getBigDecimalFromCell(row.getCell(20)));  
	    	                    ctAssy.setALLOW_TOTAL(getBigDecimalFromCell(row.getCell(21)));  
	    	                    ctAssy.setOP_TIME_NORMAL_SHIFT_0(getBigDecimalFromCell(row.getCell(22)));
	    	                    ctAssy.setOP_TIME_NORMAL_SHIFT_1(getBigDecimalFromCell(row.getCell(23)));  
	    	                    ctAssy.setOP_TIME_NORMAL_SHIFT_2(getBigDecimalFromCell(row.getCell(24))); 
	    	                    ctAssy.setOP_TIME_SHIFT_FRIDAY(getBigDecimalFromCell(row.getCell(25))); 
	    	                    ctAssy.setOP_TIME_TOTAL_NORMAL_SHIFT(getBigDecimalFromCell(row.getCell(26)));  
	    	                    ctAssy.setOP_TIME_TOTAL_SHIFT_FRIDAY(getBigDecimalFromCell(row.getCell(27))); 
	    	                    ctAssy.setKAPS_NORMAL_SHIFT_0(getBigDecimalFromCell(row.getCell(28)));  
	    	                    ctAssy.setKAPS_NORMAL_SHIFT_1(getBigDecimalFromCell(row.getCell(29))); 
	    	                    ctAssy.setKAPS_NORMAL_SHIFT_2(getBigDecimalFromCell(row.getCell(30))); 
	    	                    ctAssy.setKAPS_SHIFT_FRIDAY(getBigDecimalFromCell(row.getCell(31))); 
	    	                    ctAssy.setKAPS_TOTAL_NORMAL_SHIFT(getBigDecimalFromCell(row.getCell(32))); 
	    	                    ctAssy.setKAPS_TOTAL_SHIFT_FRIDAY(getBigDecimalFromCell(row.getCell(33))); 
	    	                    ctAssy.setWAKTU_TOTAL_CT_NORMAL(getBigDecimalFromCell(row.getCell(34))); 
	    	                    ctAssy.setWAKTU_TOTAL_CT_FRIDAY(getBigDecimalFromCell(row.getCell(34)));  
	    	                    ctAssy.setSTATUS(BigDecimal.valueOf(1));  
	    	                    ctAssy.setCREATION_DATE(new Date());  
	    	                    ctAssy.setLAST_UPDATE_DATE(new Date());  
	    	                }

	    	                ctAssyServiceImpl.saveCTAssy(ctAssy);
	    	                ctAssys.add(ctAssy);
	    	            }
	    	        }

	    	        response = new Response(new Date(), HttpStatus.OK.value(), null, "File processed and data saved", req.getRequestURI(), ctAssys);

	    	    } catch (IOException e) {
	    	        response = new Response(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "Error processing file", req.getRequestURI(), null);
	    	    }
//	        } else {
//	            throw new ResourceNotFoundException("User not found");
//	        }
//	    } catch (Exception e) {
//	        throw new ResourceNotFoundException("JWT token is not valid or expired");
//	    }
	    
	    return response;
	}

	private BigDecimal getBigDecimalFromCell(Cell cell) {
	    if (cell == null) {
	        return null;
	    }
	    switch (cell.getCellType()) {
	        case STRING:
	            String value = cell.getStringCellValue();
	            try {
	                return new BigDecimal(value);
	            } catch (NumberFormatException e) {
	                System.out.println("Invalid number format in cell: " + value);
	                return null; // Handle it as needed
	            }
	        case NUMERIC:
	            return BigDecimal.valueOf(cell.getNumericCellValue());
	        default:
	            return null; // Handle it as needed
	    }
	}

	private String getStringFromCell(Cell cell) {
	    if (cell == null) {
	        return null;
	    }
	    return cell.getStringCellValue();
	}
	
    @GetMapping("/exportCTAssyExcel")
    public ResponseEntity<InputStreamResource> exportCTAssyExcel() throws IOException {
        String filename = "MASTER_CT_ASSY_DATA.xlsx";

        // Generate the Excel data using the service method
        ByteArrayInputStream data = ctAssyServiceImpl.exportCtAssyExcel();
        InputStreamResource file = new InputStreamResource(data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file);
    }

}
