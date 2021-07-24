package controllers

import (
	models "IBM-Call-for-code-2021-zero-hunger-solution/Microservices/feedback-service/models"
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/jinzhu/gorm"
)

//create problem record
func CreateProblem(c *gin.Context) {
	db := c.MustGet("db").(*gorm.DB)

	var input models.CreateProblemInput
	if err := c.ShouldBindJSON(&input); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	//create problem
	problem := models.Problem{ProblemRating: input.ProblemRating, Details: input.Details, ProblemId: input.ProblemId}
	db.Create(&problem)
	c.JSON(http.StatusOK, gin.H{"data": problem})
}

//find problem by problemId /problem/:id
func FindProblem(c *gin.Context) {
	db := c.MustGet("db").(*gorm.DB)

	//get model
	var problem models.Problem
	if err := db.Where("problem_id = ?", c.Param("id")).First(&problem).Error; err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Record not found!"})
		return
	}

	c.JSON(http.StatusOK, gin.H{"data": problem})
}

//update problem
func UpdateProblem(c *gin.Context) {
	db := c.MustGet("db").(*gorm.DB)
	//get model
	var problem models.Problem
	if err := db.Where("problem_id = ?", c.Param("id")).First(&problem).Error; err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Record not found!"})
		return
	}
	var input models.CreateProblemInput
	if err := c.ShouldBindJSON(&input); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	db.Model(&problem).Updates(input)
	c.JSON(http.StatusOK, gin.H{"data": problem})
}

//delete problem /problem/:id
func DeleteProblem(c *gin.Context) {
	db := c.MustGet("db").(*gorm.DB)
	//get model
	var problem models.Problem
	if err := db.Where("problem_id = ?", c.Param("id")).First(&problem).Error; err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Record not found!"})
		return
	}
	db.Delete(&problem)
	c.JSON(http.StatusOK, gin.H{"data": true})
}
