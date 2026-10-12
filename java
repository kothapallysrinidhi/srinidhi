document.getElementById('adventure-form').addEventListener('submit', async function(e) {
    e.preventDefault();
    
    // Get user inputs
    const city = document.getElementById('city').value;
    const budget = document.getElementById('budget').value;
    const interests = document.getElementById('interests').value;

    // Show loading, hide results
    document.getElementById('loading').classList.remove('hidden');
    document.getElementById('result').classList.add('hidden');
    document.getElementById('result').innerHTML = '';

    // ⚠️ IMPORTANT: Paste your actual Google Gemini API key below
    const API_KEY = "YOUR_GEMINI_API_KEY_HERE"; 

    const prompt = `You are a travel agent. A user is in ${city}, has a ${budget} budget, and likes ${interests}. Create a fun 1-day micro-adventure itinerary. Return the response STRICTLY as a JSON object with this exact format: {"morning": "text here", "afternoon": "text here", "evening": "text here"}. Do not use markdown backticks.`;

    const apiUrl = `https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=${API_KEY}`;

    try {
        const response = await fetch(apiUrl, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                contents: [{ parts: [{ text: prompt }] }]
            })
        });

        const data = await response.json();
        let aiText = data.candidates[0].content.parts[0].text;
        
        // Clean up the response just in case AI adds backticks
        aiText = aiText.replace(/```json/g, '').replace(/```/g, '').trim();
        
        const itinerary = JSON.parse(aiText);

        // Display the result
        const resultDiv = document.getElementById('result');
        resultDiv.innerHTML = `
            <div class="result-section">
                <h3>🌅 Morning</h3>
                <p>${itinerary.morning}</p>
            </div>
            <div class="result-section">
                <h3>☀️ Afternoon</h3>
                <p>${itinerary.afternoon}</p>
            </div>
            <div class="result-section">
                <h3>🌙 Evening</h3>
                <p>${itinerary.evening}</p>
            </div>
        `;
        
        document.getElementById('loading').classList.add('hidden');
        resultDiv.classList.remove('hidden');

    } catch (error) {
        console.error("Error:", error);
        document.getElementById('loading').classList.add('hidden');
        alert("Failed to generate adventure. Check your API key and console.");
    }
});