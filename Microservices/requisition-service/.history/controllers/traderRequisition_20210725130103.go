package controllers

import (
	models "IBM-Call-for-code-2021-zero-hunger-solution/Microservices/requisition-service/models"
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/jinzhu/gorm"
)

//get all requisitions
func FindRequisitions(c *gin.Context) {
	db := c.MustGet("db").(*gorm.DB)

	var requisitions []models.TraderRequisition
	db.Find(&requisitions)

	c.JSON(http.StatusOK, gin.H{"data": requisitions})
}

//create requisition record
func CreateRequisition(c *gin.Context) {
	db := c.MustGet("db").(*gorm.DB)

	var input models.CreateTraderRequisitionInput
	if err := c.ShouldBindJSON(&input); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	//create requisition
	requisition := models.TraderRequisition{ProductType: input.ProductType, Quantity: input.Quantity, DeliveryLocation: input.DeliveryLocation, ExpectedDeliveryDate: input.ExpectedDeliveryDate, SpecialInstructions: input.SpecialInstructions, Repeats: input.Repeats, RepeatDate: input.RepeatDate, CreatorId: input.CreatorId}
	db.Create(&requisition)
	c.JSON(http.StatusOK, gin.H{"data": requisition})
}

//find requisitions by id /requisition/:id
func FindRequisition(c *gin.Context) {
	db := c.MustGet("db").(*gorm.DB)

	//get model
	var requisition models.TraderRequisition
	if err := db.Where("id = ?", c.Param("id")).First(&requisition).Error; err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Record not found!"})
		return
	}

	c.JSON(http.StatusOK, gin.H{"data": requisition})
}

//update requisition
func UpdateRequisitions(c *gin.Context) {
	db := c.MustGet("db").(*gorm.DB)
	//get model
	var requisition models.TraderRequisition
	if err := db.Where("id = ?", c.Param("id")).First(&requisition).Error; err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Record not found!"})
		return
	}
	var input models.CreateTraderRequisitionInput
	if err := c.ShouldBindJSON(&input); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	db.Model(&requisition).Updates(input)
	c.JSON(http.StatusOK, gin.H{"data": requisition})
}

//delete requisition /requisition/:id
func DeleteRequisition(c *gin.Context) {
	db := c.MustGet("db").(*gorm.DB)
	//get model
	var requisition models.TraderRequisition
	if err := db.Where("id = ?", c.Param("id")).First(&requisition).Error; err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Record not found!"})
		return
	}
	db.Delete(&requisition)
	c.JSON(http.StatusOK, gin.H{"data": true})
}
