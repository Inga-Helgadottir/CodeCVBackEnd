# This is a repository I created as a back end for my code CV
This project is to show that I can build a fully functioning REST API
with authentication using Java Spring boot and Jwt authentication.

To make it easier to visualize, I have also created an EER-diagram 
and SQL statements to create the database and dummy users.

I am currently looking into hosting it free online, if that works out,
my next steps will be to establish a communication with my front end.

## Here are some relevant links

- [Website hosted with Netlify](https://inga-helgadottir-code-cv.netlify.app/)
- [GitHub code for the front end](https://github.com/Inga-Helgadottir/CodeCV)
- [EER diagram](src/EER-diagram/EER-diagram.png)
- [SQL Scripts to create the DB](src/SQL/CreateSchemaAndTables.sql)
- [SQL Scripts to create test users](src/SQL/AddDummyData.sql)

# Content
[Roles](#Roles)

[Folder structure](#Folder-structure)
- [Entities](#Entities)
- [Controller](#Controllers)
- [Dto](#DTOs)
- [Entity](#Entities)
- [exceptions](#Exceptions)
- [Mapper](#Mapper)
- [Repository](#Repository)
- [Security](#Security)
- [Service](#Service)
- [Impl](#impl)
- [Resources](#Resources)
- [Tests](#Tests)

[Endpoints](#Endpoints)

## Roles
I have 2 roles admin and employee, but when they go through the JwtAuthenticationFilter, 
they become ROLE_ADMIN and ROLE_EMPLOYEE. 

### Admin
- Admin has full access to everything

### Employee
- Full access to their own information, except for their role
- The ability to see all shift related information
- The ability to add themselves to a shift

## Folder structure
src
- EER-diagram
- main
    - java
        - com.CodeCV.BackEnd
            - [controller](#Controllers)
            - [dto](#DTOs)
            - [entity](#Entities)
            - [exceptions](#Exceptions)
            - [mapper](#Mapper)
            - [repository](#Repository)
            - [security](#Security)
            - [service](#Service)
                - [impl](#impl)
            - BackEndApplication.java
    - [resources](#Resources)
- SQL
- [test](#Tests)
    - java
        - com.CodeCV.BackEnd
            - endpoints
            - entities

## Controllers
- LoginController
- EmployeeController
- RoleController
- ShiftController

## Entities

### Employee
- Long id;
- String first_name;
- String last_name;
- String email;
- String password;
- Role role;
- List<Shift> shifts;

### Role
- Long id;
- String role_name;
- String role_description;
- List<Employee> employees = new ArrayList<>();

### Shift
- Long id;
- String department;
- LocalDateTime start_time;
- LocalDateTime end_time;
- Long amount_of_employees_needed;
- List<Employee> employees = new ArrayList<>();

### EmployeeLogin (The input object for the login method)
- String email;
- String password;

## DTOs

### EmployeeDTO (So I can return the employee's information, without the password)
- Long id;
- String first_name;
- String last_name;
- String email;
- RoleDTO role;
- List<ShiftDTO> shifts;

### RoleDTO (Does not have an Employee reference to avoid an infinite loop)
- Long id;
- String role_name;
- String role_description;

### ShiftDTO (Does not have an Employees reference to avoid an infinite loop)
- Long id;
- String department;
- LocalDateTime start_time;
- LocalDateTime end_time;
- Long amount_of_employees_needed;
- Long how_many_employees_are_missing;

### CreateEmployeeDTO (The original role should be EMPLOYEE, so I set that as the role in the create method)
- String first_name;
- String last_name;
- String email;
- String password;

### EmployeeLoginDTO (This is what the login method returns, with a token and without the password)
- Long id;
- String first_name;
- String last_name;
- String email;
- List<ShiftDTO> shifts;
- RoleDTO role;
- String token;

### UpdatePasswordDTO (Used as the input Object when changing a password)
- String email;
- String oldPassword;
- String newPassword;

## Exceptions
- AuthenticationException 
- GlobalExceptionHandler
- ResourceNotFoundException

## Mapper
- EmployeeMapper
- RoleMapper
- ShiftMapper

## Repository
- EmployeeRepository
- RoleRepository
- ShiftRepository

## Security
- JwtAuthenticationFilter
- JwtUtil
- SecurityConfig

## Service
- EmployeeService
- LoginService
- RoleService
- ShiftService

### impl
- EmployeeServiceImpl
- LoginServiceImpl
- RoleServiceImpl
- ShiftServiceImpl

## Resources
- application.properties
- application-test.properties

### Tests
- endpoints
    - LoginEndpointTest
    - EmployeeEndpointTest
    - RoleEndpointTest
    - ShiftEndpointTest
- entities
    - EntityTest

## Endpoints

- [Login to the system](#httplocalhost8080login)

Employees
- [Create employee](#httplocalhost8080employeescreate)
- [Get all employees](#httplocalhost8080employeesall)
- [Get employee by id](#httplocalhost8080employeesgetByIdid)
- [Get my own info](#httplocalhost8080employeesmyInfo)
- [Get all employees on shift](#httplocalhost8080employeesemployeesOnShiftid)
- [Get all employee shifts](#httplocalhost8080employeesemployeeShiftsid)
- [Add an employee to a shift](#httplocalhost8080employeesaddEmployeeToShiftemployeeIdshiftId)
- [Update employee](#httplocalhost8080employeesupdate)
- [Update employee password](#httplocalhost8080employeesupdatePassword)
- [Delete employee](#httplocalhost8080employeesdeleteid)

Shifts
- [Create shift](#httplocalhost8080shiftscreate)
- [Get shift by id](#httplocalhost8080shiftsgetByIdid)
- [Get all shifts](#httplocalhost8080shiftsall)
- [Get all shifts that need employees](#httplocalhost8080shiftsmissingEmployees)
- [Update shift](#httplocalhost8080shiftsupdateid)
- [Delete shift](#httplocalhost8080shiftsdeleteid)

Roles
- [Create role](#httplocalhost8080rolescreate)
- [Get role by name](#httplocalhost8080rolesname)
- [Get all roles](#httplocalhost8080rolesall)
- [Update role](#httplocalhost8080rolesupdateid)
- [Delete role](#httplocalhost8080rolesdeleteid)
- [Assign a role to an employee](#httplocalhost8080assignARoleToAnEmployeeemployeeIdroleName)
- [Get employees with role](#httplocalhost8080rolesemployeesWithRoleroleName)


## http://localhost:8080/login
HTTP request method: POST
Authority needed: None
Input entity: EmployeeLogin
- Sample input
```json
{
  "email" : "alice@example.com",
  "password" : "password123"
}
```
Output entity: EmployeeLoginDTO
- Sample output
```json
{
  "id": 1,
  "first_name": "Alice",
  "last_name": "Morgan",
  "email": "alice@example.com",
  "shifts": [
    {
      "id": 1,
      "department": "Front Desk",
      "start_time": "2026-10-08T08:00:00",
      "end_time": "2026-10-08T16:00:00",
      "amount_of_employees_needed": 2,
      "how_many_employees_are_missing": 0
    }
  ],
  "role": {
    "id": 1,
    "role_name": "admin",
    "role_description": "Administrator with full access"
  },
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbGljZUBleGFtcGxlLmNvbSIsInJvbGUiOiJhZG1pbiIsImlhdCI6MTc5MTg5MDUyOCwiZXhwIjoxNzkxOTc2OTI4fQ.SbHYYY1H-OMwcZdnl4DO-Sp-wRSxnURqTJxMfD8z6Ds"
}
```


## http://localhost:8080/employees/create
HTTP request method: POST
Authority needed: ROLE_ADMIN
Input entity: CreateEmployeeDTO 
- Sample input
```json
{
  "first_name":"Francis",
  "last_name":"Baker",
  "email":"francisBaker@gmail.com",
  "password": "passwordHere"
}
```
Output entity: EmployeeDTO
- Sample output
```json
{
  "email": "francisBaker@gmail.com",
  "first_name": "Francis",
  "id": 7,
  "last_name": "Baker",
  "role": {
    "id": 2,
    "role_description": "Regular employee",
    "role_name": "employee"
  },
  "shifts": []
}
```

## http://localhost:8080/employees/all
HTTP request method: GET
Authority needed: ROLE_ADMIN
Output entity: List<EmployeeDTO>
- Sample output
```json
[
    {
        "id": 1,
        "first_name": "Alice",
        "last_name": "Morgan",
        "email": "alice@example.com",
        "role": {
            "id": 1,
            "role_name": "admin",
            "role_description": "Administrator with full access"
        },
        "shifts": [
            {
                "id": 1,
                "department": "Front Desk",
                "start_time": "2026-10-08T08:00:00",
                "end_time": "2026-10-08T16:00:00",
                "amount_of_employees_needed": 2,
                "how_many_employees_are_missing": 0
            }
        ]
    },
    {
        "id": 2,
        "first_name": "Ben",
        "last_name": "Carter",
        "email": "ben@example.com",
        "role": {
            "id": 2,
            "role_name": "employee",
            "role_description": "Regular employee"
        },
        "shifts": [
            {
                "id": 1,
                "department": "Front Desk",
                "start_time": "2026-10-08T08:00:00",
                "end_time": "2026-10-08T16:00:00",
                "amount_of_employees_needed": 2,
                "how_many_employees_are_missing": 0
            },
            {
                "id": 2,
                "department": "Kitchen",
                "start_time": "2026-10-08T10:00:00",
                "end_time": "2026-10-08T18:00:00",
                "amount_of_employees_needed": 2,
                "how_many_employees_are_missing": 1
            }
        ]
    }
]
```

## http://localhost:8080/employees/getById/{id}
HTTP request method: GET
Authority needed: ROLE_ADMIN
Output entity: EmployeeDTO
- Sample output
```json
{
    "id": 1,
    "first_name": "Alice",
    "last_name": "Morgan",
    "email": "alice@example.com",
    "role": {
        "id": 1,
        "role_name": "admin",
        "role_description": "Administrator with full access"
    },
    "shifts": [
        {
            "id": 1,
            "department": "Front Desk",
            "start_time": "2026-10-08T08:00:00",
            "end_time": "2026-10-08T16:00:00",
            "amount_of_employees_needed": 2,
            "how_many_employees_are_missing": 0
        }
    ]
}
```

## http://localhost:8080/employees/myInfo
HTTP request method: GET
Authority needed: ROLE_ADMIN or ROLE_EMPLOYEE (It gives you the information of the person in the provided token)
Output entity: EmployeeDTO
- Sample output
```json
{
  "id": 1,
  "first_name": "Alice",
  "last_name": "Morgan",
  "email": "alice@example.com",
  "role": {
    "id": 1,
    "role_name": "admin",
    "role_description": "Administrator with full access"
  },
  "shifts": [
    {
      "id": 1,
      "department": "Front Desk",
      "start_time": "2026-10-08T08:00:00",
      "end_time": "2026-10-08T16:00:00",
      "amount_of_employees_needed": 2,
      "how_many_employees_are_missing": 0
    }
  ]
}
```

## http://localhost:8080/employees/employeesOnShift/{id}
HTTP request method: GET
Authority needed: ROLE_ADMIN or ROLE_EMPLOYEE (ROLE_EMPLOYEE can only see the employees on their own shifts)
Output entity: List<EmployeeDTO>
- Sample output
```json
[
    {
        "email": "cr@example.com",
        "first_name": "Chloe",
        "id": 3,
        "last_name": "Reed",
        "role": {
            "id": 2,
            "role_description": "Regular employee",
            "role_name": "employee"
        },
        "shifts": [
            {
                "id": 3,
                "department": "Front Desk",
                "start_time": "2026-10-09T08:00:00",
                "end_time": "2026-10-09T16:00:00",
                "amount_of_employees_needed": 1,
                "how_many_employees_are_missing": -1
            }
        ]
    },
    {
        "email": "frankieman@example.com",
        "first_name": "Frank",
        "id": 7,
        "last_name": "Further",
        "role": {
            "id": 2,
            "role_description": "Regular employee",
            "role_name": "employee"
        },
        "shifts": [
            {
                "id": 3,
                "department": "Front Desk",
                "start_time": "2026-10-09T08:00:00",
                "end_time": "2026-10-09T16:00:00",
                "amount_of_employees_needed": 1,
                "how_many_employees_are_missing": -1
            }
        ]
    }
]
```

## http://localhost:8080/employees/employeeShifts/{id}
HTTP request method: GET
Authority needed: ROLE_ADMIN or ROLE_EMPLOYEE (ROLE_EMPLOYEE can only see their own shifts)
Output entity: List<ShiftDTO>
- Sample output
```json
[
    {
        "id": 1,
        "department": "Front Desk",
        "start_time": "2026-10-08T08:00:00",
        "end_time": "2026-10-08T16:00:00",
        "amount_of_employees_needed": 2,
        "how_many_employees_are_missing": 0
    },
    {
        "id": 2,
        "department": "HR",
        "start_time": "2026-11-08T08:00:00",
        "end_time": "2026-11-08T16:00:00",
        "amount_of_employees_needed": 3,
        "how_many_employees_are_missing": 1
    }
]
```

## http://localhost:8080/employees/addEmployeeToShift/{employeeId}/{shiftId}
HTTP request method: PUT
Authority needed: ROLE_ADMIN or ROLE_EMPLOYEE (ROLE_EMPLOYEE can only add themselves to shifts)
Output entity: EmployeeDTO
- Sample output
```json
{
    "id": 2,
    "first_name": "Benjamin",
    "last_name": "Carter",
    "email": "benji@example.com",
    "role": {
        "id": 1,
        "role_description": "Administrator with full access",
        "role_name": "admin"
    },
    "shifts": [
        {
            "id": 2,
            "department": "Kitchen",
            "start_time": "2026-10-08T10:00:00",
            "end_time": "2026-10-08T18:00:00",
            "amount_of_employees_needed": 2,
            "how_many_employees_are_missing": 1
        }
    ]
}
```

## http://localhost:8080/employees/update
HTTP request method: PUT
Authority needed: ROLE_ADMIN or ROLE_EMPLOYEE (ROLE_EMPLOYEE can only update their own information)
Input entity: EmployeeDTO with missing values as you can only update first_name, last_name and email here
- Sample input
```json
 {
        "id": 2,
        "first_name": "Benjamin",
        "last_name": "Carter",
        "email": "benCar@example.com"
}
```
Output entity: EmployeeDTO
- Sample output
```json
{
    "id": 2,
    "email": "benCar@example.com",
    "first_name": "Benjamin",
    "last_name": "Carter",
    "role": {
        "id": 4,
        "role_description": "Regular employee",
        "role_name": "employee"
    },
    "shifts": []
}
```

## http://localhost:8080/employees/updatePassword
HTTP request method: PUT
Authority needed: ROLE_ADMIN or ROLE_EMPLOYEE (ROLE_EMPLOYEE can only update their own password)
Input entity: UpdatePasswordDTO with missing values as you can only update the password here
- Sample input
```json
{
  "email": "fesaefedg@gmail.com",
  "oldPassword": "oldPassword",
  "newPassword": "newPasswordH3r3"
}
```
Output: String
- Sample output
```String
Password successfully changed!
```

## http://localhost:8080/employees/delete/{id}
HTTP request method: DELETE
Authority needed: ROLE_ADMIN
Output: String
- Sample output
```String
Employee deleted successfully!
```


## http://localhost:8080/shifts/create
HTTP request method: POST
Authority needed: ROLE_ADMIN
Input entity: ShiftDTO (the id is auto generated)
- Sample input
```json
{
    "department":"department number 1",
    "start_time":"2018-02-05T12:00:00.332",
    "end_time":"2018-02-05T12:30:00.33",
    "amount_of_employees_needed":2
}
```
Output entity: ShiftDTO
- Sample output
```json
{
    "id": 9,
    "department": "department number 1",
    "start_time": "2018-02-05T12:00:00.332",
    "end_time": "2018-02-05T12:30:00.33",
    "amount_of_employees_needed": 2,
    "how_many_employees_are_missing": 2
}
```

## http://localhost:8080/shifts/getById/{id}
HTTP request method: GET
Authority needed: ROLE_ADMIN
Output entity: ShiftDTO
- Sample output
```json
{
    "id": 1,
    "department": "Front Desk",
    "start_time": "2026-10-08T08:00:00",
    "end_time": "2026-10-08T16:00:00",
    "amount_of_employees_needed": 2,
    "how_many_employees_are_missing": 1
}
```

## http://localhost:8080/shifts/all
HTTP request method: GET
Authority needed: ROLE_ADMIN or ROLE_EMPLOYEE
Output entity: List<ShiftDTO>
- Sample output
```json
[
    {
        "id": 1,
        "department": "Front Desk",
        "start_time": "2026-10-08T08:00:00",
        "end_time": "2026-10-08T16:00:00",
        "amount_of_employees_needed": 2,
        "how_many_employees_are_missing": 1
    },
    {
        "id": 2,
        "department": "Kitchen",
        "start_time": "2026-10-08T10:00:00",
        "end_time": "2026-10-08T18:00:00",
        "amount_of_employees_needed": 2,
        "how_many_employees_are_missing": 1
    },
    {
        "id": 3,
        "department": "Front Desk",
        "start_time": "2026-10-09T08:00:00",
        "end_time": "2026-10-09T16:00:00",
        "amount_of_employees_needed": 1,
        "how_many_employees_are_missing": -1
    },
    {
        "id": 9,
        "department": "department number 1",
        "start_time": "2018-02-05T12:00:00",
        "end_time": "2018-02-05T12:30:00",
        "amount_of_employees_needed": 2,
        "how_many_employees_are_missing": 2
    }
]
```

## http://localhost:8080/shifts/missingEmployees
HTTP request method: GET
Authority needed: ROLE_ADMIN or ROLE_EMPLOYEE
Output entity: List<ShiftDTO>
- Sample output
```json
[
  {
    "id": 1,
    "department": "Front Desk",
    "start_time": "2026-10-08T08:00:00",
    "end_time": "2026-10-08T16:00:00",
    "amount_of_employees_needed": 2,
    "how_many_employees_are_missing": 1
  },
  {
    "id": 2,
    "department": "Kitchen",
    "start_time": "2026-10-08T10:00:00",
    "end_time": "2026-10-08T18:00:00",
    "amount_of_employees_needed": 2,
    "how_many_employees_are_missing": 1
  }
]
```

## http://localhost:8080/shifts/update/{id}
HTTP request method: PUT
Authority needed: ROLE_ADMIN
Output entity: List<ShiftDTO>
- Sample input and output (just to confirm the shift has been updated)
```json
{
    "id": 9,
    "department": "department number 11",
    "start_time": "2018-02-05T12:00:00",
    "end_time": "2018-02-05T12:30:00",
    "amount_of_employees_needed": 2,
    "how_many_employees_are_missing": 2
}
```

## http://localhost:8080/shifts/delete/{id}
HTTP request method: DELETE
Authority needed: ROLE_ADMIN
Output: String
- Sample output
```String
Shift deleted successfully!
```


## http://localhost:8080/roles/create
HTTP request method: POST
Authority needed: ROLE_ADMIN
Input entity: RoleDTO (the id is auto generated)
- Sample input
```json
{
    "role_description": "some test role thing",
    "role_name": "tester"
}
```
Output entity: RoleDTO
- Sample output
```json
{
    "id": 6,
    "role_description": "some test role thing",
    "role_name": "tester"
}
```

## http://localhost:8080/roles/{name}
HTTP request method: GET
Authority needed: ROLE_ADMIN
Output entity: RoleDTO
- Sample output
```json
{
    "id": 2,
    "role_description": "Regular employee",
    "role_name": "employee" 
}
```

## http://localhost:8080/roles/all
HTTP request method: GET
Authority needed: ROLE_ADMIN
Output entity: List<RoleDTO>
- Sample output
```json
[
    {
        "id": 1,
        "role_description": "Administrator with full access",
        "role_name": "admin"
    },
    {
        "id": 2,
        "role_description": "Regular employee",
        "role_name": "employee"
    },
    {
        "id": 6,
        "role_description": "some test role thing",
        "role_name": "tester"
    }
]
```

## http://localhost:8080/roles/update/{id}
HTTP request method: PUT
Authority needed: ROLE_ADMIN
Input and output entity: RoleDTO
- Sample input and output
```json
{
    "id": 6,
    "role_description": "Some test role thing here",
    "role_name": "Tester"
}
```

## http://localhost:8080/roles/delete/{id}
HTTP request method: DELETE
Authority needed: ROLE_ADMIN
Output: String
- Sample output
```String
Role deleted successfully!
```

## http://localhost:8080/assignARoleToAnEmployee/{employeeId}/{roleName}
HTTP request method: PUT
Authority needed: ROLE_ADMIN
Output Entity: EmployeeDTO
- Sample output
```json
{
    "email": "benji@example.com",
    "first_name": "Benjamin",
    "id": 2,
    "last_name": "Carter",
    "role": {
        "id": 2,
        "role_description": "Regular employee",
        "role_name": "employee"
    },
    "shifts": [
        {
            "id": 2,
            "department": "Kitchen",
            "start_time": "2026-10-08T10:00:00",
            "end_time": "2026-10-08T18:00:00",
            "amount_of_employees_needed": 2,
            "how_many_employees_are_missing": 1
        }
    ]
}
```

## http://localhost:8080/roles/employeesWithRole/{roleName}
HTTP request method: GET
Authority needed: ROLE_ADMIN
Output entity: List<EmployeeDTO>
- Sample output
```json
[
    {
        "id": 1,
        "first_name": "Alice",
        "last_name": "Morgan",
        "email": "alice@example.com",
        "role": {
            "id": 1,
            "role_name": "admin",
            "role_description": "Administrator with full access"
        },
        "shifts": [
            {
                "id": 1,
                "department": "Front Desk",
                "start_time": "2026-10-08T08:00:00",
                "end_time": "2026-10-08T16:00:00",
                "amount_of_employees_needed": 2,
                "how_many_employees_are_missing": 0
            }
        ]
    },
    {
        "id": 2,
        "first_name": "Ben",
        "last_name": "Carter",
        "email": "ben@example.com",
        "role": {
            "id": 1,
            "role_name": "admin",
            "role_description": "Administrator with full access"
        },
        "shifts": [
            {
            "id": 1,
            "department": "Front Desk",
            "start_time": "2026-10-08T08:00:00",
            "end_time": "2026-10-08T16:00:00",
            "amount_of_employees_needed": 2,
            "how_many_employees_are_missing": 0
            }
        ]
    }
]
```

### Endpoint tests
For each endpoint there are 2 or 3 of the following tests
- Success 
  - Called with all the correct information
- WrongToken, when there is one
  - Called with the wrong token
- NoToken
  - Called with no token
