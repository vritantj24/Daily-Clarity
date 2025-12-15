document.addEventListener('DOMContentLoaded', () => {

    // 1. Mobile Menu Toggle
    const menuToggle = document.getElementById('mobile-menu');
    const navbar = document.querySelector('.navbar');

    if (menuToggle) {
        menuToggle.addEventListener('click', () => {
            navbar.classList.toggle('active');
        });
    }

    // 2. Dynamic Date in Top Bar
    const dateElement = document.getElementById('current-date');
    if (dateElement) {
        const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
        const today = new Date();
        dateElement.textContent = today.toLocaleDateString('en-US', options);
    }

    // 3. SSE: listen for backend events
    if (!!window.EventSource) {
        const source = new EventSource('/news/stream');

        // Named event (matches .name("news-update") on backend)
        source.addEventListener('news-update', function () {
            // When event arrives, call API to get fresh data
            fetch('api/fetch-latest')
                .then(response => response.json())
                .then(data => {
                    updateHomepage(data);
                })
                .catch(err => console.error('Failed to update news', err));
        });

        source.onerror = function () {
            console.warn('SSE connection error');
        };
    } else {
        console.warn('SSE not supported; consider fallback polling');
    }

    function updateHomepage(data) {
        // 3a. Featured article
        const heroSection = document.querySelector('.hero-section');
        if(heroSection && data.featuredArticle){
            const heroImage = heroSection.querySelector('.hero-image img');
            const categoryTag = heroSection.querySelector('.category-tag');
            const heroTitleLink = heroSection.querySelector('.hero-title a');
            const heroExcerpt = heroSection.querySelector('.hero-excerpt');
            const readMoreLink = heroSection.querySelector('.read-more');

            if (heroImage) {
                heroImage.src = data.featuredArticle.imageUrl;
            }
            if (categoryTag) {
                categoryTag.textContent = data.featuredArticle.category;
            }
            if (heroTitleLink) {
                heroTitleLink.textContent = data.featuredArticle.title;
                heroTitleLink.href = data.featuredArticle.url;
            }
            if (heroExcerpt) {
                heroExcerpt.textContent = data.featuredArticle.description;
            }
            if (readMoreLink) {
                readMoreLink.href = data.featuredArticle.url;
            }

            // Ensure section is visible if it was hidden by th:if initially
            heroSection.style.display = '';
        }

        // 3b. Latest stories
        const cardsWrapper = document.querySelector('.news-cards-wrapper');
        if (cardsWrapper && Array.isArray(data.latestArticles)) {
            cardsWrapper.innerHTML = '';
            data.latestArticles.forEach(article => {
                const articleEl = document.createElement('article');
                articleEl.classList.add('news-card');
                articleEl.innerHTML = `
                    <div class="card-img">
                        <img src="${article.imageUrl}" alt="News Image">
                    </div>
                    <div class="card-body">
                        <span class="card-meta">
                            <span>${article.category}</span> |
                            <span>${article.publishedAt}</span>
                        </span>
                        <h3 class="card-title">
                            <a href="${article.url}">
                                ${article.title}
                            </a>
                        </h3>
                        <p class="card-summary">
                            ${article.description}
                        </p>
                    </div>
                `;
                cardsWrapper.appendChild(articleEl);
            });
        }

        // 3c. Trending list
        const trendingList = document.querySelector('.trending-list');
        if (trendingList && Array.isArray(data.trendingArticles)) {
            trendingList.innerHTML = '';
            data.trendingArticles.forEach((trend, idx) => {
                const li = document.createElement('li');
                li.innerHTML = `
                    <span class="trend-number">#${idx + 1}</span>
                    <a href="${trend.url}">${trend.title}</a>
                `;
                trendingList.appendChild(li);
            });
        }
    }
});