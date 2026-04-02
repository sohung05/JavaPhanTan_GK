/*
 * @ (#) Person.java          1.0        4/2/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */

package iuh.fit.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/*
 * @description:
 * @author: Truong Tran Hung
 * @date: 4/2/2026
 * @version:    1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor

public abstract class Person implements Serializable {
    protected String id;
    protected String name;
    protected String phone;


}
