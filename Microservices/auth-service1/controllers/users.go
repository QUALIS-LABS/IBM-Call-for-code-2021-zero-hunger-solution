package controllers

import (
	models "IBM-Call-for-code-2021-zero-hunger-solution/Microservices/auth-service/models"
	"net/http"
	"os"

	"github.com/dgrijalva/jwt-go"
	"github.com/gin-gonic/gin"
	"github.com/jinzhu/gorm"
	"golang.org/x/crypto/bcrypt"
)

//Register create new user /register
func Register(c *gin.Context) {
	db := c.MustGet("db").(*gorm.DB)

	var input models.CreateUserInput
	if err := c.ShouldBindJSON(&input); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error(), "message": "signUp", "status": false})
		return
	}
	//ensure unique
	var user models.User
	if err := db.Where("email = ?", input.Email).First(&user).Error; err == nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Email Taken!", "message": "signUp", "status": false})
		return
	}
	//create user
	hashedPassword, _ := bcrypt.GenerateFromPassword([]byte(input.Password), bcrypt.DefaultCost)
	hashPass := string(hashedPassword)
	tk := models.Token{Email: input.Email}
	token := jwt.NewWithClaims(jwt.GetSigningMethod("HS256"), tk)
	tokenString, _ := token.SignedString([]byte(os.Getenv("token_password")))
	user2 := models.User{UserName: input.UserName, Email: input.Email, Password: hashPass, UserType: input.UserType, Token: tokenString}
	db.Create(&user2)
	c.JSON(http.StatusOK, gin.H{"User": user2, "message": "signUp", "status": true})
}

//Authenticate user /authenticate/
func Authenticate(c *gin.Context) {
	db := c.MustGet("db").(*gorm.DB)

	//get model
	var input models.UserLogin
	if err := c.ShouldBindJSON(&input); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error(), "message": "login", "status": false})
		return
	}
	//get user
	var user models.User
	if err := db.Where("email = ?", input.Email).First(&user).Error; err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Email does not exist!", "message": "login", "status": false})
		return
	}
	err := bcrypt.CompareHashAndPassword([]byte(user.Password), []byte(input.Password))
	if err != nil && err == bcrypt.ErrMismatchedHashAndPassword { //Password does not match!
		c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid login credentials. Please try again", "message": "login", "status": false})
		return
	}
	tk := models.Token{Email: input.Email}
	token := jwt.NewWithClaims(jwt.GetSigningMethod("HS256"), tk)
	tokenString, _ := token.SignedString([]byte(os.Getenv("token_password")))
	upToken := models.User{Token: tokenString}
	db.Model(&user).Updates(upToken)
	c.JSON(http.StatusOK, gin.H{"User": user, "message": "login", "status": true})
}

//UpdateUserDetails changes
func UpdateUserDetails(c *gin.Context) {
	db := c.MustGet("db").(*gorm.DB)
	//get model
	var user models.User
	if err := db.Where("id = ?", c.Param("id")).First(&user).Error; err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Record not found!", "message": "update", "status": false})
		return
	}
	var input models.CreateUserInput
	if err := c.ShouldBindJSON(&input); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error(), "message": "update", "status": false})
		return
	}
	db.Model(&user).Updates(input)
	c.JSON(http.StatusOK, gin.H{"data": input, "message": "update", "status": true})
}

//DeleteUser /user/:id
func DeleteUser(c *gin.Context) {
	db := c.MustGet("db").(*gorm.DB)
	//get model
	var user models.User
	if err := db.Where("id = ?", c.Param("id")).First(&user).Error; err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Record not found!", "message": "delete", "status": false})
		return
	}
	db.Delete(&user)
	c.JSON(http.StatusOK, gin.H{"message": "delete", "status": true})
}

//DeleteUserByEmail /deleteuser
func DeleteUserByEmail(c *gin.Context) {
	db := c.MustGet("db").(*gorm.DB)
	//get model
	var user models.User
	var input models.DeleteUserByEmail
	if err := c.ShouldBindJSON(&input); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error(), "message": "delete", "status": false})
		return
	}
	if err := db.Where("email = ?", input.Email).First(&user).Error; err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Record not found!", "message": "delete", "status": false})
		return
	}
	db.Delete(&user)
	c.JSON(http.StatusOK, gin.H{"message": "delete", "status": true})
}
