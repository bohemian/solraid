mermaid.initialize({startOnLoad: true});

const outputPanelSelector = 'tbody[class~="shared-eventsviewer-list-body"]';
const umlSelector = 'span[data-path="uml"]';

const umlStyle = document.createElement('style');
umlStyle.id = 'splunk-uml-renderer-style';
umlStyle.textContent = `
/* CSS Nesting is very new and will not work on all browsers, consult https://caniuse.com/css-nesting for support */
${umlSelector} {
  .label {
      text-transform: none;
  }

  foreignObject > div {
      display: flex !important;
      height: 100%;
      justify-content: center;
      align-items: center;

      p {
          margin: 0;
      }
  }

  em {
      font-style: italic !important;
  }

  /* removes splunk hover state backgrounds */
  &.h,
  .h {
      background-color: transparent !important;
      border-top: none !important;
      border-bottom: none !important;
      color: inherit !important;
  }

  .h.edgeLabel {
      background-color: #e8e8e8 !important;
  }

  @media (prefers-color-scheme: dark) {
      .flowchart-link,
      .marker {
        stroke: #fff !important;
        fill: #fff !important;
      }
  }
}
`;

document.body.append(umlStyle);

const renderingObserver = new MutationObserver(function (mutations) {
    mutations.forEach((mutation) => {
        mutation.addedNodes.forEach((node) => {
            // if node is an element, check for uml nodes
            if (node.nodeType === 1) {
                node.querySelectorAll(umlSelector).forEach(renderUml);
            }
        });
    });
});

// when output panel created, render uml and add hook for future updates
const creationObserver = new MutationObserver(function (mutations) {
    if (mutations) {
        const outputPanel = document.querySelector(outputPanelSelector);
        if (outputPanel) {
            renderingObserver.observe(outputPanel, {childList: true, subtree: true})
            creationObserver.disconnect();
        }
    }
});

creationObserver.observe(document, {childList: true, subtree: true});

function renderUml(umlNode) {
    if (!umlNode.textContent.startsWith('flowchart')) return;

    let diagramDefinition = umlNode.textContent.replaceAll(';', '\n');
    let diagramId = 'UML' + Math.random().toString().substring(2);
    mermaid.render(diagramId, diagramDefinition).then((svgCode) => {
        umlNode.innerHTML = svgCode.svg;
    });
}
