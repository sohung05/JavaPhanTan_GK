/*
 * @ (#) Patient.java          1.0        4/2/2026
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
public class Patient extends Person implements Serializable {
    private Gender gender;
    private String dateOfBirth;
    private String address;
    private List<Treatment> treatments;

    public Patient(String id, String name, String phone, Gender gender, String dateOfBirth, String address) {
        super(id, name, phone);
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }
}
