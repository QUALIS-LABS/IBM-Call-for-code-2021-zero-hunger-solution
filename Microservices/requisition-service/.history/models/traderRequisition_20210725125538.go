package models

import (
	"time"
)

//User details
type Requisition struct {
	ID                   uint       `json:"id" gorm:"primary_key";"AUTO_INCREMENT"`
	ProductType          string     `json:"productType"`
	Quantity             int        `json:"quantity"`
	DeliveryLocation     string     `json:"deliveryLocation"`
	ExpectedDeliveryDate string     `json:"expectedDeliveryDate"`
	SpecialInstructions  string     `json:"specialInstructions";sql:"-"`
	Repeats              bool       `json:"repeats"`
	RepeatDate           string     `json:"repeatDate"`
	CreatorId            uint       `json:"creatorId"`
	CreatedAt            time.Time  `json:"createdAt"`
	UpdatedAt            time.Time  `json:"updatedAt"`
	DeletedAt            *time.Time `json:"deletedAt"`
}

//CreateUserInput fields
type CreateRequisitionInput struct {
	ProductType          string `json:"productType"`
	Quantity             int    `json:"quantity"`
	DeliveryLocation     string `json:"deliveryLocation"`
	ExpectedDeliveryDate string `json:"expectedDeliveryDate"`
	SpecialInstructions  string `json:"specialInstructions";sql:"-"`
	Repeats              bool   `json:"repeats"`
	RepeatDate           string `json:"repeatDate"`
	CreatorId            uint   `json:"creatorId"`
}
