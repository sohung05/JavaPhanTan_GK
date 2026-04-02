// =======================
// 1. TẠO UNIQUE CONSTRAINT (INDEX)
// =======================
CREATE CONSTRAINT dep_id IF NOT EXISTS
FOR (d:Department) REQUIRE d.id IS UNIQUE;

CREATE CONSTRAINT doc_id IF NOT EXISTS
FOR (d:Doctor) REQUIRE d.id IS UNIQUE;

CREATE CONSTRAINT patient_id IF NOT EXISTS
FOR (p:Patient) REQUIRE p.id IS UNIQUE;


// =======================
// 2. LOAD DỮ LIỆU TỪ CSV
// =======================

// Department
LOAD CSV WITH HEADERS FROM "file:///hospital/departments.csv" AS row
WITH row WHERE row.id IS NOT NULL
MERGE (d:Department {id: row.id})
SET d.name = row.name,
    d.location = row.location;


// Doctor
LOAD CSV WITH HEADERS FROM "file:///hospital/doctors.csv" AS row
WITH row WHERE row.ID IS NOT NULL
MERGE (d:Doctor {id: row.ID})
SET d.name = row.Name,
    d.phone = row.Phone,
    d.speciality = row.Speciality,
    d.departmentID = row.DepartmentID;


// Patient
LOAD CSV WITH HEADERS FROM "file:///hospital/patients.csv" AS row
WITH row WHERE row.ID IS NOT NULL
MERGE (p:Patient {id: row.ID})
SET p.name = row.Name,
    p.phone = row.Phone,
    p.gender = row.Gender,
    p.address = row.Address,
    p.dateOfBirth = row.DateOfBirth;


// =======================
// 3. TẠO RELATIONSHIP
// =======================

// Doctor - BELONG_TO -> Department
MATCH (doc:Doctor), (dep:Department)//lấy tất cả trong Doctor và 
WHERE doc.departmentID = dep.id
MERGE (doc)-[:BELONG_TO]->(dep);


// Patient - BE_TREATED -> Doctor
LOAD CSV WITH HEADERS FROM "file:///hospital/treatments.csv" AS row
WITH row WHERE row.DoctorID IS NOT NULL AND row.PatientID IS NOT NULL
MATCH (d:Doctor {id: row.DoctorID})
MATCH (p:Patient {id: row.PatientID})
MERGE (p)-[r:BE_TREATED]->(d)
SET r.startDate = row.StartDate,
    r.endDate = row.EndDate,
    r.diagnosis = row.Diagnosis;