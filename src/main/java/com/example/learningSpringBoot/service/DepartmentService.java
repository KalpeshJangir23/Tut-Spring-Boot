package com.example.learningSpringBoot.service;

import com.example.learningSpringBoot.entity.Department;

import java.util.List;

public interface DepartmentService {
  public Department saveDepartment(Department department);

  public List<Department> fetchDepartmentList();

  public  Department getDepartmentById(Long departmentId);

  public  void deleteDepartmentById(Long departmentId);

  public  Department updateDepartment(Long departmentId, Department department);
}
