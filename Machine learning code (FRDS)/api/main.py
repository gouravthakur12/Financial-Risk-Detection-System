from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
import pickle
import numpy as np
import os
from typing import List

app = FastAPI(title="Fraud Detection ML Service")

# CORS configuration
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Global model variable
model = None

class Transaction(BaseModel):
    amount: float
    time: float
    features: List[float]

class PredictionResponse(BaseModel):
    fraud_probability: float
    is_fraud: bool
    risk_score: float

@app.on_event("startup")
async def load_model():
    """Load the ML model on startup"""
    global model
    model_path = os.path.join("saved_model", "fraud_model.pkl")
    
    if os.path.exists(model_path):
        try:
            with open(model_path, 'rb') as f:
                model = pickle.load(f)
            print(f"Model loaded successfully from {model_path}")
        except Exception as e:
            print(f"Error loading model: {e}")
            model = None
    else:
        print(f"Model file not found at {model_path}. Service will run without model.")
        model = None

@app.get("/")
async def root():
    """Root endpoint"""
    return {
        "service": "Fraud Detection ML Service",
        "status": "running",
        "model_loaded": model is not None
    }

@app.get("/health")
async def health_check():
    """Health check endpoint"""
    return {
        "status": "healthy",
        "model_loaded": model is not None
    }

@app.post("/predict", response_model=PredictionResponse)
async def predict(transaction: Transaction):
    """Predict fraud probability for a transaction"""
    
    if model is None:
        # Return mock prediction if model not loaded
        fraud_prob = 0.15
        return PredictionResponse(
            fraud_probability=fraud_prob,
            is_fraud=fraud_prob > 0.5,
            risk_score=fraud_prob * 100
        )
    
    try:
        # Prepare features
        features = np.array([transaction.features]).reshape(1, -1)
        
        # Get prediction
        fraud_prob = float(model.predict_proba(features)[0][1])
        
        return PredictionResponse(
            fraud_probability=fraud_prob,
            is_fraud=fraud_prob > 0.5,
            risk_score=fraud_prob * 100
        )
    
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Prediction error: {str(e)}")

@app.get("/model/info")
async def model_info():
    """Get model information"""
    if model is None:
        return {"error": "Model not loaded"}
    
    return {
        "model_type": str(type(model).__name__),
        "model_loaded": True
    }

if __name__ == "__main__":
    import uvicorn
    port = int(os.getenv("PORT", 8000))
    uvicorn.run(app, host="0.0.0.0", port=port)
