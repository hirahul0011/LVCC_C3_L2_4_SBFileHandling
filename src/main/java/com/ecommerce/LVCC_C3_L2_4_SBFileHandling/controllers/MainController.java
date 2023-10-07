package com.ecommerce.LVCC_C3_L2_4_SBFileHandling.controllers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class MainController {
	
	@RequestMapping(value = "/")
	public String index() {
	    return "index.html";
	}
	    
	@RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String fileUpload(@RequestParam("fileToUpload") MultipartFile file) {
	        String result = "File was uploaded successfully";
	        
	        try {
	        	  File convertFile = new File("E:\\Personal\\"+file.getOriginalFilename());
			      convertFile.createNewFile();
			      FileOutputStream fout = new FileOutputStream(convertFile);
			      fout.write(file.getBytes());
			      fout.close();
	      
	        } catch (IOException iex) {
	              result = "Error " + iex.getMessage();
	                
	        } finally {
	              return result;
	        }
	}	
	    
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public ResponseEntity<Object> downloadFile() throws IOException  {
	        String fileName = "static/dump.txt";
			ClassLoader classLoader = new MainController().getClass().getClassLoader();
			
			File file = new File(classLoader.getResource(fileName).getFile());
			
			HttpHeaders headers = new HttpHeaders();
			
//			Method 1 Working successfully and saving the file in download  
			InputStreamResource resource = new InputStreamResource(new FileInputStream(file));			      
			headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
			
//			Method 2 Not tries
//			Path path = Paths.get(file.getAbsolutePath());
//		    ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
			
			
			
//		   headers.add("Content-Disposition", String.format("attachment; filename=\\%s\\", file.getName()));
//		   headers.add("Content-Disposition", String.format("attachment; filename=E:\\Personal\\", file.getName()));
		   headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		   headers.add("Pragma", "no-cache");
		   headers.add("Expires", "0");
		      
		   ResponseEntity<Object>
		   responseEntity = ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(
		      MediaType.parseMediaType("application/txt")).body(resource);
		   

//	Need to work on how to move and delete the file as soon as it is downloaded	   
//		   try (BufferedInputStream inputStream = new BufferedInputStream(new URL("C:/Users/Lenovo/Downloads"+file.getName()).openStream());
//				   FileOutputStream fileOS = new FileOutputStream("E:\\Personal\\"+file.getName())) {
//				     byte data[] = new byte[1024];
//				     int byteContent;
//				     while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
//				         fileOS.write(data, 0, byteContent);
//				     }
//				 } catch (IOException e) {
//				     // handles IO exceptions
//				 }
		   
//		   File src = new File("C:\\Users\\Lenovo\\Downloads\\"+file.getName());
//	        File dest = new File("E:\\Personal\\"+file.getName());	        
//			Files.copy(src.toPath(), dest.toPath(),StandardCopyOption.REPLACE_EXISTING);			
//		   File myObj = new File("C:\\Users\\Lenovo\\Downloads\\"+file.getName());		   
//		   boolean b=myObj.delete();		   
		   
		   return responseEntity;
	}

}
