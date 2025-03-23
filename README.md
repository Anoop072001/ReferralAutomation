# FindMeAJob

🚀 Welcome to **FindMeAJob** – your gateway to seamless job discovery and referrals! This project is built using **Spring Boot** and integrates **OpenAI** and **RapidAPI** services to enhance job search and application automation.

## 🔥 Features
- Parses resumes to extract key details like **experience, skills, and education**.
- Generates **LinkedIn referral messages** to boost your chances of getting noticed.
- Utilizes **OpenAI API** for intelligent text generation.
- Leverages **RapidAPI** for additional job-related functionalities.

## 🛠 Configuration
This application requires environment variables to be set before running:

```properties
spring.application.name=findmeajob
openai.api.key=${OPENAI_API_KEY}
rapidapi.key=${RAPIDAPI_API_KEY}
server.port=${PORT:8080}
```

- **OPENAI_API_KEY**: Your OpenAI API key for AI-powered text generation.
- **RAPIDAPI_API_KEY**: API key for accessing job-related services.
- **PORT**: (Optional) Define a custom port; defaults to `8080`.

## 🚀 Getting Started
1. Clone this repository:
   ```sh
   git clone https://github.com/your-repo/findmeajob.git
   ```
2. Navigate to the project directory:
   ```sh
   cd findmeajob
   ```
3. Set up environment variables for API keys.
4. Run the application:
   ```sh
   mvn spring-boot:run
   ```

## 🎯 Future Enhancements
- **Automated LinkedIn job applications** based on parsed resume data.
- **Advanced AI-driven job recommendations**.
- **User dashboard for tracking referrals and applications**.

🚀 **FindMeAJob** is just getting started. Stay tuned for more updates and enhancements!

