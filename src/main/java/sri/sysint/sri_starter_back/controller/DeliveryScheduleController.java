package sri.sysint.sri_starter_back.controller;

import static sri.sysint.sri_starter_back.security.SecurityConstants.SECRET;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
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
import sri.sysint.sri_starter_back.model.Building;
import sri.sysint.sri_starter_back.model.DeliverySchedule;
import sri.sysint.sri_starter_back.model.Response;
import sri.sysint.sri_starter_back.service.DeliveryScheduleServiceImpl;

@CrossOrigin(maxAge = 3600)
@RestController
public class DeliveryScheduleController {
		
	private Response response;	

	@Autowired
	private DeliveryScheduleServiceImpl deliveryScheduleServiceImpl;
	
	@PersistenceContext	
	private EntityManager em;
	
//START - GET MAPPING
	@GetMapping("/getAllDeliverySchedules")
	public Response getAllDeliverySchedules(final HttpServletRequest req) throws ResourceNotFoundException {
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
	        	List<DeliverySchedule> deliverySchedules = new ArrayList<>();
	    	    deliverySchedules = deliveryScheduleServiceImpl.getAllDeliverySchedules();

	    	    response = new Response(
	    	        new Date(),
	    	        HttpStatus.OK.value(),
	    	        null,
	    	        HttpStatus.OK.getReasonPhrase(),
	    	        req.getRequestURI(),
	    	        deliverySchedules
	    	    );
	        } else {
	            throw new ResourceNotFoundException("User not found");
	        }
	    } catch (Exception e) {
	        throw new ResourceNotFoundException("JWT token is not valid or expired");
	    }

	    return response;
	}
	
	@GetMapping("/getDeliveryScheduleById/{id}")
	public Response getDeliveryScheduleById(final HttpServletRequest req, @PathVariable BigDecimal id) throws ResourceNotFoundException {
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
	        	Optional<DeliverySchedule> deliverySchedule = Optional.of(new DeliverySchedule());
	    	    deliverySchedule = deliveryScheduleServiceImpl.getDeliveryScheduleById(id);

	    	    response = new Response(
	    	        new Date(),
	    	        HttpStatus.OK.value(),
	    	        null,
	    	        HttpStatus.OK.getReasonPhrase(),
	    	        req.getRequestURI(),
	    	        deliverySchedule
	    	    );
	        } else {
	            throw new ResourceNotFoundException("User not found");
	        }
	    } catch (Exception e) {
	        throw new ResourceNotFoundException("JWT token is not valid or expired");
	    }

	    return response;
	}
//END - GET MAPPING

//START - POST MAPPING
	@PostMapping("/saveDeliverySchedule")
	public Response saveDeliverySchedule(final HttpServletRequest req, @RequestBody DeliverySchedule deliverySchedule) throws ResourceNotFoundException {
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
	        	DeliverySchedule savedDeliverySchedule = deliveryScheduleServiceImpl.saveDeliverySchedule(deliverySchedule);

	    	    response = new Response(
	    	        new Date(),
	    	        HttpStatus.OK.value(),
	    	        null,
	    	        HttpStatus.OK.getReasonPhrase(),
	    	        req.getRequestURI(),
	    	        savedDeliverySchedule
	    	    );
	        } else {
	            throw new ResourceNotFoundException("User not found");
	        }
	    } catch (Exception e) {
	        throw new ResourceNotFoundException("JWT token is not valid or expired");
	    }

	    return response;
	}
	
	@PostMapping("/updateDeliverySchedule")
	public Response updateDeliverySchedule(final HttpServletRequest req, @RequestBody DeliverySchedule deliverySchedule) throws ResourceNotFoundException {
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
	        	DeliverySchedule updatedDeliverySchedule = deliveryScheduleServiceImpl.updateDeliverySchedule(deliverySchedule);

	    	    response = new Response(
	    	        new Date(),
	    	        HttpStatus.OK.value(),
	    	        null,
	    	        HttpStatus.OK.getReasonPhrase(),
	    	        req.getRequestURI(),
	    	        updatedDeliverySchedule
	    	    );
	        } else {
	            throw new ResourceNotFoundException("User not found");
	        }
	    } catch (Exception e) {
	        throw new ResourceNotFoundException("JWT token is not valid or expired");
	    }

	    return response;
	}
	
	@PostMapping("/deleteDeliverySchedule")
	public Response deleteDeliverySchedule(final HttpServletRequest req, @RequestBody DeliverySchedule deliverySchedule) throws ResourceNotFoundException {
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
	        	DeliverySchedule deletedDeliverySchedule = deliveryScheduleServiceImpl.deleteDeliverySchedule(deliverySchedule);

	    	    response = new Response(
	    	        new Date(),
	    	        HttpStatus.OK.value(),
	    	        null,
	    	        HttpStatus.OK.getReasonPhrase(),
	    	        req.getRequestURI(),
	    	        deletedDeliverySchedule
	    	    );
	        } else {
	            throw new ResourceNotFoundException("User not found");
	        }
	    } catch (Exception e) {
	        throw new ResourceNotFoundException("JWT token is not valid or expired");
	    }

	    return response;
	}
	
	@PostMapping("/restoreDeliverySchedule")
	public Response restoreDeliverySchedule(final HttpServletRequest req, @RequestBody DeliverySchedule deliverySchedule) throws ResourceNotFoundException {
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
	            // Assuming you have a method to restore a delivery schedule
	            DeliverySchedule restoredDeliverySchedule = deliveryScheduleServiceImpl.restoreDeliverySchedule(deliverySchedule);

	            response = new Response(
	                new Date(),
	                HttpStatus.OK.value(),
	                null,
	                HttpStatus.OK.getReasonPhrase(),
	                req.getRequestURI(),
	                restoredDeliverySchedule
	            );
	        } else {
	            throw new ResourceNotFoundException("User not found");
	        }
	    } catch (Exception e) {
	        throw new ResourceNotFoundException("JWT token is not valid or expired");
	    }

	    return response;
	}
	
	@PostMapping("/saveDeliverySchedulesExcel")
	public Response saveDeliverySchedulesExcelFile(@RequestParam("file") MultipartFile file, final HttpServletRequest req) throws ResourceNotFoundException {
	    if (file.isEmpty()) {
	        return new Response(new Date(), HttpStatus.BAD_REQUEST.value(), null, "No file uploaded", req.getRequestURI(), null);
	    }
	    
	    deliveryScheduleServiceImpl.deleteAllDeliverySchedules();

	    try (InputStream inputStream = file.getInputStream()) {
	        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
	        XSSFSheet sheet = workbook.getSheetAt(0);
	        
	        List<DeliverySchedule> deliverySchedules = new ArrayList<>();

	        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
	            Row row = sheet.getRow(i);
	            if (row != null) {
	                DeliverySchedule deliverySchedule = new DeliverySchedule();

	                Cell effectiveTimeCell = row.getCell(1);
	                Cell dateIssuedCell = row.getCell(2);
	                Cell categoryCell = row.getCell(3);

	                if (effectiveTimeCell != null && dateIssuedCell != null && categoryCell != null) {
	                    deliverySchedule.setDS_ID(deliveryScheduleServiceImpl.getNewId());

	                    Date effectiveTime = getDateFromCell(effectiveTimeCell);
	                    if (effectiveTime != null) {
	                        deliverySchedule.setEFFECTIVE_TIME(effectiveTime);
	                    } else {
	                        continue;
	                    }

	                    Date dateIssued = getDateFromCell(dateIssuedCell);
	                    if (dateIssued != null) {
	                        deliverySchedule.setDATE_ISSUED(dateIssued);
	                    } else {
	                        continue;
	                    }

	                    deliverySchedule.setCATEGORY(categoryCell.getStringCellValue());

	                    deliverySchedule.setSTATUS(BigDecimal.valueOf(1));
	                    deliverySchedule.setCREATION_DATE(new Date());
	                    deliverySchedule.setLAST_UPDATE_DATE(new Date());
	                }

	                deliveryScheduleServiceImpl.saveDeliverySchedule(deliverySchedule);
	                deliverySchedules.add(deliverySchedule);
	            }
	        }

	        response = new Response(new Date(), HttpStatus.OK.value(), null, "File processed and data saved", req.getRequestURI(), deliverySchedules);

	    } catch (IOException | ParseException e) {
	        response = new Response(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "Error processing file", req.getRequestURI(), null);
	    }

	    return response;
	}
	
	private Date getDateFromCell(Cell cell) throws ParseException {
	    if (cell == null) {
	        return null;
	    }

	    if (cell.getCellType() == CellType.NUMERIC) {
	        if (DateUtil.isCellDateFormatted(cell)) {
	            return cell.getDateCellValue();
	        } else {
	            return null;
	        }
	    } else if (cell.getCellType() == CellType.STRING) {
	        String dateStr = cell.getStringCellValue();
	        if (dateStr == null || dateStr.isEmpty()) {
	            return null;
	        }
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        sdf.setLenient(false);
	        return sdf.parse(dateStr);
	    }

	    return null;
	}

	
    @GetMapping("/exportDeliveryScheduleExcel")
    public ResponseEntity<InputStreamResource> exportDeliveryScheduleExcel() throws IOException {
        String filename = "MASTER_DELIVERY_SCHEDULE.xlsx";

        ByteArrayInputStream data = deliveryScheduleServiceImpl.exportDeliverySchedulesExcel();
        InputStreamResource file = new InputStreamResource(data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file);
    }
	        

	
	
//END - POST MAPPING
//START - PUT MAPPING
//END - PUT MAPPING
//START - DELETE MAPPING
//END - DELETE MAPPING
//START - PROCEDURE
//END - PROCEDURE
}
