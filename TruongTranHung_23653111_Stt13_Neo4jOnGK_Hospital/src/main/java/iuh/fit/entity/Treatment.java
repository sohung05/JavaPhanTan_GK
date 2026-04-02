/*
 * @ (#) Treatment.java          1.0        4/2/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */

package iuh.fit.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.print.Doc;
import javax.swing.*;
import java.io.Serializable;
import java.security.PrivateKey;
import java.time.LocalDate;

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
public class Treatment implements Serializable {

    private LocalDate startDate;
    private LocalDate endDate;
    private String diagnosis;
    private Doctor doctor;
    private Patient patient;

}
