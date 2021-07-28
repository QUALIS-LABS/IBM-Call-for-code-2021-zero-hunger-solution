package main

import (
	controllers "IBM-Call-for-code-2021-zero-hunger-solution/Microservices/trip-service/controllers"
	"IBM-Call-for-code-2021-zero-hunger-solution/Microservices/trip-service/models"

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

	//problem routes

	r.GET("/trips", controllers.FindTrips)

	r.POST("/trip", controllers.CreateTrip)

	r.GET("/trip/:id", controllers.FindTrip)

	r.PATCH("/trip/:id", controllers.UpdateTrip)

	r.DELETE("/trip/:id", controllers.DeleteTrip)

	r.Run(":4080")
}
