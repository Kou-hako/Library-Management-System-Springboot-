package com.lms.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.Entity.Address;


public interface AddressRepository extends JpaRepository<Address, Long>{

  Optional<Address> findByAuthorId(Long id);
}
