package com.dl1803.identity.validator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// chứa 2 para : 1/ annotation mà validator này chịu trách nhiệm xử lí | 2/ kdl mà data mà ta muốn validate
public class DobValidator implements ConstraintValidator<DobContraint, LocalDate> {

    private int min;

    @Override // khởi tạo mỗi khi constraint này đc khởi tạo -> get đc những thông số CỦA annotation đó
    // ( ví dụ min : 18 -> bước này sẽ lấy được những giá trị thông số này) diễn ra trước hàm ísValid.
    public void initialize(DobContraint constraintAnnotation) { // tham số đầu vào là ddtuong annotation đã khai báo

        //        constraintAnnotation -> chính là -> @DobConstraint(min=18)
        /* chứa : message
        min
        groups
        payload*/
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min(); // lấy từ đầu vào annotation -> min =18
    }

    @Override // hàm xử lí việc kiểm tra data này có đúng với thông số yêu cầu không (ví dụ min max ,....) (chỉ chạy 1
    // lần trc khi validate)
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        // value là giá trị từ request / context là đối tượng giúp tạo và tùy chỉnh lỗi message khi validation  (vì đã
        // dùng hàm mess bên class annotation rồi -> k cần viết ở đâu)
        // Objects là lớp đối tượng chứa các hàm kiểm tra đtuong
        if (Objects.isNull(value)) return true; // nếu đầu vào k có field dob -> mặc định là true

        long years = ChronoUnit.YEARS.between(
                value,
                LocalDate.now()); // tính số năm bằng lớp ChronoUnit -> độ lớn giữa ngày tháng năm của request và ngày
        // tháng năm hiện tại

        return years >= min;
    }
}
