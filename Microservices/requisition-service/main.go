package main

import (
	controllers "IBM-Call-for-code-2021-zero-hunger-solution/Microservices/requisition-service/controllers"
	"IBM-Call-for-code-2021-zero-hunger-solution/Microservices/requisition-service/models"

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
	r.GET("/requisitions", controllers.FindRequisitions)

	r.POST("/requisition", controllers.CreateRequisition)

	r.GET("/requisition/:id", controllers.FindRequisition)

	r.PATCH("/requisition/:id", controllers.UpdateRequisitions)

	r.DELETE("/requisition/:id", controllers.DeleteRequisition)

	r.Run(":4000")
}
