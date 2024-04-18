package com.carlease.project.car;
import com.carlease.project.user.User;

import java.util.List;

public interface ICarService {
    List<Car> findAll();
    Car findById(long id);

}
