/*
 * @ (#) Doctor.java          1.0        4/2/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */

package iuh.fit.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/*
 * @description:
 * @author: Truong Tran Hung
 * @date: 4/2/2026
 * @version:    1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Doctor extends Person implements Serializable {
    private String speciality;
    private Department department;
    private List<Treatment> treatments;
    public Doctor(String id, String name, String phone, String speciality) {
        super(id, name, phone);
        this.speciality = speciality;
    }
}
