# ShopWise AI — 4-Hour Hackathon Project

**An AI-powered personal shopping assistant that understands natural language queries and recommends products with intelligent trade-off detection.**

## Problem Statement

Users struggle to find products that match their complex, multi-faceted needs. The system needs to:
- Understand natural language shopping intent
- Extract structured requirements (budget, preferences, priorities)
- Search real product databases intelligently
- Rank products by personalized priorities
- Explain recommendations clearly
- Detect and highlight useful trade-offs

## Architecture & Tech Stack

### Frontend
- **React** with **Vite**
- **Tailwind CSS** for styling

### Backend
- **Node.js** with **Express**
- API routes for recommendation pipeline

### AI & LLM
- **Google Gemini API** for requirement extraction and explanations

### Database
- **Supabase PostgreSQL** for product catalog, searches, and recommendations

## Core Flow

```
User natural-language query
       ↓
Gemini: Extract structured requirements
       ↓
Backend: Deterministic filtering & scoring
       ↓
Database: Retrieve filtered, ranked products
       ↓
Calculate match percentages
       ↓
Rank by: Best Overall / Best Value / Best Performance
       ↓
Gemini: Generate "Why this product?" explanations
       ↓
Frontend: Display recommendations + comparison view
       ↓
Save search & recommendations to Supabase
```

## MVP Features

1. ✅ Natural-language shopping input
2. ✅ AI requirement extraction (Gemini)
3. ✅ Budget-aware filtering
4. ✅ Personalized recommendation scoring (deterministic backend logic)
5. ✅ Match percentage for each product
6. ✅ Multiple ranking strategies: Best Overall / Best Value / Best Performance
7. ✅ "Why this product?" AI-generated explanation
8. ✅ Product comparison for 2–3 products side-by-side
9. ✅ Smart trade-off detection (e.g., "high performance requires higher budget")
10. ✅ Supabase persistence of searches and recommendations

## Product Categories (MVP)

- Laptops
- Smartphones
- Headphones

## Database Schema

### `products`
```
id | category | name | price | specs (JSONB) | ratings (JSONB) | created_at
```

### `searches`
```
id | query (text) | extracted_requirements (JSONB) | created_at
```

### `recommendations`
```
id | search_id | product_id | match_percentage | rank_type (OVERALL/VALUE/PERFORMANCE) | explanation | created_at
```

## Important Architectural Decisions

⚠️ **Critical:** Gemini should NOT determine numerical product ranking.
- Gemini extracts intent from natural language and generates explanations
- Backend performs all deterministic filtering, scoring, and ranking using database data
- This ensures reproducibility and control over ranking factors

## Out of Scope (NOT building)

- Authentication / User accounts
- Payment processing
- Shopping cart / Checkout
- Admin dashboard
- Web scraping (using pre-seeded product data)
- Complex ecommerce features

## Implementation Roadmap (Priority Order)

### Phase 1: Scaffold & Setup
- [ ] Initialize React + Vite frontend
- [ ] Initialize Express backend
- [ ] Configure Tailwind CSS
- [ ] Set up Supabase project & schema

### Phase 2: Product Database
- [ ] Create products table
- [ ] Seed with ~50–100 laptops, smartphones, headphones
- [ ] Add specs (JSONB): CPU, RAM, storage, battery, camera, etc.
- [ ] Add ratings: average, count, reviews

### Phase 3: Backend AI Pipeline
- [ ] Gemini API integration for requirement extraction
- [ ] Deterministic filtering algorithm (budget, category, priority tags)
- [ ] Scoring logic for match percentage
- [ ] Best Overall / Best Value / Best Performance ranking logic
- [ ] Gemini API for "Why this product?" explanations

### Phase 4: Recommendation Features
- [ ] Product comparison view (2–3 products)
- [ ] Smart trade-off detection
- [ ] Recommendations table schema & insertion

### Phase 5: Frontend & Polish
- [ ] Query input UI
- [ ] Results display (recommendation cards)
- [ ] Comparison UI
- [ ] Loading & error states
- [ ] Demo data & manual testing

### Phase 6: Persistence
- [ ] Save searches to Supabase
- [ ] Save recommendations to Supabase
- [ ] Query history view (optional MVP+)

## Environment Variables

Create a `.env` file in the backend root with:

```
GEMINI_API_KEY=your_gemini_api_key
SUPABASE_URL=your_supabase_project_url
SUPABASE_KEY=your_supabase_anon_key
```

## Development Setup (for teammates)

```bash
# Clone the repository
git clone https://github.com/Inzamam18/VibeCode.git
cd VibeCode

# Setup backend
cd backend
npm install
# Create .env with API keys (see above)
npm run dev

# In a new terminal, setup frontend
cd frontend
npm install
npm run dev
```

Frontend will be at `http://localhost:5173`  
Backend will be at `http://localhost:3000`

## API Endpoints (Backend)

- `POST /api/recommend` — Submit natural-language query, get recommendations
- `POST /api/compare` — Compare 2–3 products
- `GET /api/searches` — Fetch search history (for demo purposes)
- `GET /api/recommendations/:searchId` — Fetch recommendations for a search

## File Structure (Expected)

```
VibeCode/
├── backend/
│   ├── src/
│   │   ├── routes/
│   │   │   ├── recommend.js
│   │   │   ├── compare.js
│   │   │   └── search.js
│   │   ├── services/
│   │   │   ├── gemini.js (AI requirement extraction)
│   │   │   ├── scorer.js (deterministic ranking)
│   │   │   └── supabase.js (database)
│   │   └── server.js
│   ├── .env
│   └── package.json
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   │   ├── QueryInput.jsx
│   │   │   ├── RecommendationCard.jsx
│   │   │   ├── ComparisonView.jsx
│   │   │   └── TradeOffAlert.jsx
│   │   ├── pages/
│   │   │   └── Home.jsx
│   │   ├── App.jsx
│   │   └── main.jsx
│   ├── tailwind.config.js
│   └── package.json
├── README.md
└── .git/
```

## Next Steps

1. **Scaffold** the frontend and backend
2. **Initialize Supabase** and create the product schema
3. **Seed product data** (laptops, smartphones, headphones)
4. **Implement Gemini integration** for requirement extraction
5. **Build the scoring algorithm** (deterministic backend ranking)
6. **Wire the frontend** to the API
7. **Add recommendation explanations** and comparison logic
8. **Test & polish** for MVP presentation

---

**Status:** Repository initialized. Ready for team development.  
**Hackathon Duration:** 4 hours  
**Target:** Polished, working MVP with real database and AI integration.