package main

import (
	controllers "IBM-Call-for-code-2021-zero-hunger-solution/Microservices/auth-service/controllers"
	"IBM-Call-for-code-2021-zero-hunger-solution/Microservices/auth-service/models"

	"github.com/gin-contrib/cors"
	"github.com/gin-gonic/gin"
)

func main() {
	r := gin.Default()
	config := cors.DefaultConfig()
	config.AllowAllOrigins = true
	config.AllowCredentials = true
	config.AddAllowHeaders("authorization")
	r.Use(cors.New(config))

	db := models.SetupModels()
	r.Use(func(c *gin.Context) {
		c.Set("db", db)
		c.Next()
	})

	//routes
	r.POST("/register", controllers.Register)

	r.POST("/login", controllers.Authenticate)

	r.PATCH("/user/:id", controllers.UpdateUserDetails)

	r.DELETE("/user/:id", controllers.DeleteUser)

	r.POST("/deleteuser", controllers.DeleteUserByEmail)

	r.Run(":3030")
}
