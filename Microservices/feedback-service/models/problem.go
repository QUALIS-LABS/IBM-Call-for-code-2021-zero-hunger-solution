package models

import (
	"time"
)

//Problem details
type Problem struct {
	ID            uint       `json:"id" gorm:"primary_key";"AUTO_INCREMENT"`
	ProblemRating int        `json:"problemRating"`
	Details       string     `json:"details"`
	ProblemId     uint       `json:"problemId"`
	CreatedAt     time.Time  `json:"createdAt"`
	UpdatedAt     time.Time  `json:"updatedAt"`
	DeletedAt     *time.Time `json:"deletedAt"`
}

//CreateProblemInput fields
type CreateProblemInput struct {
	ProblemRating int    `json:"problemRating"`
	Details       string `json:"details"`
	ProblemId     uint   `json:"problemId"`
}
