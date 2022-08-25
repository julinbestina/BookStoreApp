package com.bl.repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.bl.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	@Query("SELECT u FROM Customer u WHERE u.emailId = ?1 and u.password = ?2")
	public Optional<Customer> findByEmailIdAndByPassword(String emailId, String password);

	public Optional<Customer> findByEmailId(String emailId);

}
