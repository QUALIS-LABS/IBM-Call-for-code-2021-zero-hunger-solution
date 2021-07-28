package controllers

import (
	models "IBM-Call-for-code-2021-zero-hunger-solution/Microservices/trip-service/models"
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/jinzhu/gorm"
)

//get all trips
func FindTrips(c *gin.Context) {
	db := c.MustGet("db").(*gorm.DB)

	var trips []models.Trip
	db.Find(&trips)

	c.JSON(http.StatusOK, gin.H{"data": trips})
}

//create trip record
func CreateTrip(c *gin.Context) {
	db := c.MustGet("db").(*gorm.DB)

	var input models.CreateTripInput
	if err := c.ShouldBindJSON(&input); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	//create trip
	trip := models.FarmerReq{ProductType: input.ProductType, Quantity: input.Quantity, ExpectedPickupDate: input.ExpectedPickupDate, SpecialInstructions: input.SpecialInstructions, CreatorId: input.CreatorId, PickupLocation: input.PickupLocation, Status: input.Status}
	db.Create(&requisition)
	c.JSON(http.StatusOK, gin.H{"data": requisition})
}

//find requisitions by id /requisition/:id
func FindFarmerRequisition(c *gin.Context) {
	db := c.MustGet("db").(*gorm.DB)

	//get model
	var requisition models.FarmerRequisition
	if err := db.Where("id = ?", c.Param("id")).First(&requisition).Error; err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Record not found!"})
		return
	}

	c.JSON(http.StatusOK, gin.H{"data": requisition})
}

//update requisition
func UpdateFarmerRequisitions(c *gin.Context) {
	db := c.MustGet("db").(*gorm.DB)
	//get model
	var requisition models.FarmerRequisition
	if err := db.Where("id = ?", c.Param("id")).First(&requisition).Error; err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Record not found!"})
		return
	}
	var input models.CreateFarmerRequisitionInput
	if err := c.ShouldBindJSON(&input); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	db.Model(&requisition).Updates(input)
	c.JSON(http.StatusOK, gin.H{"data": requisition})
}

//delete requisition /requisition/:id
func DeleteFarmerRequisition(c *gin.Context) {
	db := c.MustGet("db").(*gorm.DB)
	//get model
	var requisition models.FarmerRequisition
	if err := db.Where("id = ?", c.Param("id")).First(&requisition).Error; err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Record not found!"})
		return
	}
	db.Delete(&requisition)
	c.JSON(http.StatusOK, gin.H{"data": true})
}
