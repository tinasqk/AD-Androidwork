# Mental Health Journaling App

This Android application helps users track their mental health by providing two main features:

## Features

### 1. Mood Tracking
- Log mood three times a day (morning, afternoon, evening)
- Choose from 5 mood levels: Very Good, Good, Neutral, Bad, Very Bad
- Calendar view to track mood history

### 2. Lifestyle Tracking
- Track daily habits that may affect mental health:
  - Sleep hours
  - Water intake
  - Overtime work
  - Exercise minutes
  - Stress level
  - Social interaction hours
- Add notes for each day

## Architecture

The app follows MVVM (Model-View-ViewModel) architecture:
- **Model**: Data classes for mood and lifestyle entries
- **ViewModel**: Manages UI state and business logic
- **View**: Jetpack Compose UI components

## Technologies Used

- Kotlin
- Jetpack Compose for UI
- Navigation Compose for navigation
- ViewModel for state management

## Usage

The app has a bottom navigation bar with two tabs:
1. Mood tracking screen
2. Lifestyle tracking screen

Users can navigate between these screens to log their daily mood and lifestyle habits. 