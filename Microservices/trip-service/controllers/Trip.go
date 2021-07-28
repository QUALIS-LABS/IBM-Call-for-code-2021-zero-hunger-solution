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
	trip := models.Trip{
		DriverID:            input.DriverID,
		TripName:            input.TripName,
		TraderRequisitionID: input.TraderRequisitionID,
		FarmerRequisitionID: input.FarmerRequisitionID,
		Status:              "active"}
	db.Create(&trip)
	c.JSON(http.StatusOK, gin.H{"data": trip})
}

//find trip by id /trip/:id
func FindTrip(c *gin.Context) {
	db := c.MustGet("db").(*gorm.DB)

	//get model
	var trip models.Trip
	if err := db.Where("id = ?", c.Param("id")).First(&trip).Error; err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Record not found!"})
		return
	}

	c.JSON(http.StatusOK, gin.H{"data": trip})
}

//update trip
func UpdateTrip(c *gin.Context) {
	db := c.MustGet("db").(*gorm.DB)
	//get model
	var trip models.Trip
	if err := db.Where("id = ?", c.Param("id")).First(&trip).Error; err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Record not found!"})
		return
	}
	var input models.CreateTripInput
	if err := c.ShouldBindJSON(&input); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	db.Model(&trip).Updates(input)
	c.JSON(http.StatusOK, gin.H{"data": trip})
}

//delete trip /trip/:id
func DeleteTrip(c *gin.Context) {
	db := c.MustGet("db").(*gorm.DB)
	//get model
	var trip models.Trip
	if err := db.Where("id = ?", c.Param("id")).First(&trip).Error; err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Record not found!"})
		return
	}
	db.Delete(&trip)
	c.JSON(http.StatusOK, gin.H{"data": true})
}
