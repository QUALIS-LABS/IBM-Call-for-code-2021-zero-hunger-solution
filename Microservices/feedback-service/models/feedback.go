package models

import (
	"time"
)

//Feedback details
type Feedback struct {
	ID        uint       `json:"id" gorm:"primary_key";"AUTO_INCREMENT"`
	Rating    int        `json:"rating"`
	Notes     string     `json:"notes"`
	CreatorId uint       `json:"creatorId"` //rated user id
	CreatedAt time.Time  `json:"createdAt"`
	UpdatedAt time.Time  `json:"updatedAt"`
	DeletedAt *time.Time `json:"deletedAt"`
}

//CreateFeedbackInput fields
type CreateFeedbackInput struct {
	Rating    int    `json:"rating"`
	Notes     string `json:"notes"`
	CreatorId uint   `json:"creatorId"`
}

type AverageRating struct {
	CreatorId     uint    `json:"creatorId"`
	AverageRating float64 `json:"averageRating"`
	Sums          int     `json:"sums"`
	Counts        int     `json:"counts"`
}
