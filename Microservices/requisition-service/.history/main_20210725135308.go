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
	r.GET("/traderRequisitions", controllers.FindRequisitions)

	r.POST("/traderRequisition", controllers.CreateRequisition)

	r.GET("/traderRequisition/:id", controllers.FindRequisition)

	r.PATCH("/traderRequisition/:id", controllers.UpdateRequisitions)

	r.DELETE("/traderRequisition/:id", controllers.DeleteRequisition)

	r.Run(":4000")
}
