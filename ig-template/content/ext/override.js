const fetchAndPopulateGithubData = () => {
  const url = "https://api.github.com/repos/navikt/fhir";
  const cached = sessionStorage.getItem(url);
  if (cached) {
    const data = JSON.parse(cached);
    populateDataToDom(data);
  } else {
    fetch(url)
      .then((response) => response.json())
      .then((data) => {
        const { stargazers_count, watchers_count } = data;
        sessionStorage.setItem(
          url,
          JSON.stringify({ stargazers_count, watchers_count })
        );
        populateDataToDom({ stargazers_count, watchers_count });
      });
  }
};
const createLiElement = (text) => {
  const tagNode = document.createElement("li");
  tagNode.appendChild(document.createTextNode(text));
  return tagNode;
};

const populateDataToDom = (data) => {
  const { stargazers_count, watchers_count } = data;
  var element = document.getElementById("nav-source-repo-features");
  if (element) {
    element.appendChild(createLiElement(stargazers_count + " Stars"));
    element.appendChild(createLiElement(watchers_count + " Watchers"));
  }
};

window.addEventListener("load", function () {
  fetchAndPopulateGithubData();
});
