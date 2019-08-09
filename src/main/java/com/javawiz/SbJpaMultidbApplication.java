package com.javawiz;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import com.javawiz.primary.entity.User;
import com.javawiz.primary.repository.UserRepository;
import com.javawiz.secondary.entity.Product;
import com.javawiz.secondary.repository.ProductRepository;

@SpringBootApplication
public class SbJpaMultidbApplication {
	
	private static final Logger LOG = LoggerFactory.getLogger(SbJpaMultidbApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SbJpaMultidbApplication.class, args);
	}
	
	@Autowired
    private UserRepository userRepository;
 
    @Autowired
    private ProductRepository productRepository;
    
    @Bean
    public CommandLineRunner tableInfo() {
    	return (args) -> {
    		getAllUsers();
    		whenCreatingUsersWithSameEmail_thenRollback();
    		product();
    	};
    }
    
    @Transactional("primaryTransactionManager")
    public void getAllUsers() {
        User user = new User();
        user.setName("John");
        user.setEmail("john1@test.com");
        user.setAge(20);
        user = userRepository.save(user);
        userRepository.findAll().forEach(data->{
        	LOG.debug("{}", data);
        });
    }
    
    @Transactional("primaryTransactionManager")
    public void whenCreatingUsersWithSameEmail_thenRollback() {
        User user1 = new User();
        user1.setName("John");
        user1.setEmail("john2@test.com");
        user1.setAge(20);
        user1 = userRepository.save(user1);
        userRepository.findById(user1.getId());
        LOG.debug("{}", userRepository.findById(user1.getId()));
 
        User user2 = new User();
        user2.setName("Tom");
        user2.setEmail("john3@test.com");
        user2.setAge(10);
        try {
            user2 = userRepository.save(user2);
        } catch (DataIntegrityViolationException e) {
        }
        LOG.debug("{}", userRepository.findById(user2.getId()));
    }
    
    @Transactional("secondaryTransactionManager")
    public void product() {
        Product product = new Product();
		product.setProductId("1234");
        product.setDescription("Spring Framework Guru Shirt");
        product.setPrice(new BigDecimal("18.95"));
        product = productRepository.save(product);
        
        Product fetchedProduct = productRepository.findById(product.getId()).get();
        LOG.debug("{}", fetchedProduct);
        
        //update description and save
        fetchedProduct.setDescription("New Description");
        productRepository.save(fetchedProduct);
        
      //get from DB, should be updated
        Product fetchedUpdatedProduct = productRepository.findById(fetchedProduct.getId()).get();
        LOG.debug("{}", fetchedUpdatedProduct);
        
        //verify count of products in DB
        long productCount = productRepository.count();
        LOG.debug("{}", productCount);
        
		productRepository.findAll().stream().forEach(data -> {
			LOG.debug("{}", data);
		});
    }
}
