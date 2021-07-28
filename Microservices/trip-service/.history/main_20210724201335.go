package main

import (
	controllers "IBM-Call-for-code-2021-zero-hunger-solution/Microservices/feedback-service/controllers"
	"IBM-Call-for-code-2021-zero-hunger-solution/Microservices/feedback-service/models"

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

	//feedbackroutes
	r.GET("/feedback/:id", controllers.FindFeedback)

	r.POST("/feedback", controllers.CreateFeedback)

	r.GET("/averageRating/:id", controllers.FindAverageRating)

	r.DELETE("/feedback/:id", controllers.DeleteFeedback)

	//problem routes

	r.POST("/problem", controllers.CreateProblem)

	r.GET("/problem/:id", controllers.FindProblem)

	r.PATCH("/problem/:id", controllers.UpdateProblem)

	r.DELETE("/problem/:id", controllers.DeleteProblem)

	r.Run(":4040")
}
