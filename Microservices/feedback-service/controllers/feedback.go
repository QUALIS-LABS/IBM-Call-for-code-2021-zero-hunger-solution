package controllers

import (
	models "IBM-Call-for-code-2021-zero-hunger-solution/Microservices/feedback-service/models"
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/jinzhu/gorm"
)

//get all feedback for creatorId
func FindFeedback(c *gin.Context) {
	db := c.MustGet("db").(*gorm.DB)

	var feedback []models.Feedback
	if err := db.Where("creator_id = ?", c.Param("id")).Find(&feedback).Error; err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Record not found!"})
		return
	}

	c.JSON(http.StatusOK, gin.H{"data": feedback})
}

//create feedback record
func CreateFeedback(c *gin.Context) {
	db := c.MustGet("db").(*gorm.DB)

	var input models.CreateFeedbackInput
	if err := c.ShouldBindJSON(&input); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	//create feedback
	feedback := models.Feedback{Rating: input.Rating, Notes: input.Notes, CreatorId: input.CreatorId}
	db.Create(&feedback)
	c.JSON(http.StatusOK, gin.H{"data": feedback})
}

//find user average rating by creatorId /averagerating/:id
func FindAverageRating(c *gin.Context) {
	db := c.MustGet("db").(*gorm.DB)

	//get model
	var feedback models.Feedback
	var averageRating models.AverageRating
	if err := db.Model(&feedback).Select("creator_id, sum(rating) as sums, count(rating) as counts").Where("creator_id = ?", c.Param("id")).Group("creator_id").Scan(&averageRating).Error; err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Record not found!"})
		return
	}
	averageRating.AverageRating = float64(averageRating.Sums) / float64(averageRating.Counts)

	c.JSON(http.StatusOK, gin.H{"data": averageRating})
}

//delete feedback /feedback/:id
func DeleteFeedback(c *gin.Context) {
	db := c.MustGet("db").(*gorm.DB)
	//get model
	var feedback models.Feedback
	if err := db.Where("creator_id = ?", c.Param("id")).First(&feedback).Error; err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Record not found!"})
		return
	}
	db.Delete(&feedback)
	c.JSON(http.StatusOK, gin.H{"data": true})
}
