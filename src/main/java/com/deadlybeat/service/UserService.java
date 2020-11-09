package com.deadlybeat.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.deadlybeat.entity.User;
import com.deadlybeat.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;
	
	Object target;
	
	Logger logger=LoggerFactory.getLogger(UserService.class);
	
	//act as Asnc method
	@Async
	public CompletableFuture<List<User>> saveUsers(MultipartFile file) throws Exception{
		//start time for thread execution
		long startTime=System.currentTimeMillis();
		
		System.out.println("start of save method.");
		List<User> users=parseCsvFile(file);
		logger.info("saving list of users of size {}"+users.size(),""+Thread.currentThread().getName());
		users=repository.saveAll(users);
		
		//end time to thread execution
		long endTime=System.currentTimeMillis();
		logger.info("Total time for parsing {}",(endTime-startTime));
		System.out.println("end of save method.");
		return CompletableFuture.completedFuture(users);
		
	}
	
	@Async
	public CompletableFuture<List<User>> findAllUsers(){
		logger.info("get list of user by thread name:-"+Thread.currentThread().getName());
		List<User> users= repository.findAll();
		return CompletableFuture.completedFuture(users);
	}
	
	
	//Utility method called by save method to parse and stire the CSV file data 
	private List<User> parseCsvFile(MultipartFile file) throws Exception{
		final List<User> users= new ArrayList<>();
		
		try{
			try(final BufferedReader br=new BufferedReader(new InputStreamReader(file.getInputStream()))){
				String line;
				//get every line and check each fow in CSV file and add data into list
				while((line=br.readLine())!=null) {
					final String[] data=line.split(",");
					final User user=new User();
					user.setName(data[0]);
					user.setEmail(data[1]);
					user.setGender(data[2]);
					users.add(user);
				}
			return users;
			}
		}catch( final IOException ex) {
			logger.error("failed to parse CSV file",ex);
			throw new Exception("failed to parse CSV file",ex);
		}
	}
}
