/*
 * @ (#) Department.java          1.0        4/2/2026
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
public class Department implements Serializable {
    private String id;
    private String name;
    private String location;

    private List<Doctor>  doctors;
}
