package models

import (
	"time"

	"github.com/dgrijalva/jwt-go"
)

//User details
type User struct {
	ID        uint       `json:"id" gorm:"primary_key";"AUTO_INCREMENT"`
	UserName  string     `json:"userName"`
	Email     string     `json:"email"`
	Password  string     `json:"password"`
	UserType  string     `json:"userType"`
	Token     string     `json:"token";sql:"-"`
	CreatedAt time.Time  `json:"createdAt"`
	UpdatedAt time.Time  `json:"updatedAt"`
	DeletedAt *time.Time `json:"deletedAt"`
}

//CreateUserInput fields
type CreateUserInput struct {
	UserName string `json:"userName"`
	Email    string `json:"email"`
	Password string `json:"password"`
	UserType string `json:"userType"`
}

//UserLogin fields
type UserLogin struct {
	Email    string `json:"email"`
	Password string `json:"password"`
}

//Token for login
type Token struct {
	Email string
	Role  string
	jwt.StandardClaims
}

type DeleteUserByEmail struct {
	Email string `json:"email"`
}
