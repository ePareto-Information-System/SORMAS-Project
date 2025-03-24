window.downloadHtmlAsImage = function (imageType = "image/png", fileName = "downloaded-image") {
    const elementId = localStorage.getItem("chartID");
    let element = document.getElementById(elementId);
    if (!element) {
        console.error("Element not found:", elementId);
        return;
    }

    let extension = ""
    switch (imageType) {
        case "image/png":
            extension = ".png";
            break;
        case "image/jpeg":
            extension = ".jpg";
            break;
        case "image/svg+xml":
            extension = ".svg";
            break;
        default:
            extension= ".jpg";
    }

    html2canvas(element)
        .then(canvas => {
            let link = document.createElement("a");
            link.href = canvas.toDataURL(imageType);
            link.download = fileName + extension;
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        })
        .catch(error => {
            console.error("Error capturing element as image:", error);
        });
};


window.downloadPDF = function (fileName = "downloaded-pdf") {
    const elementId = localStorage.getItem("chartID");
    let element = document.getElementById(elementId);
    if (!element) {
        console.error("Element not found:", elementId);
        return;
    }
    const { jsPDF } = window.jspdf;
    let pdf = new jsPDF();

    html2canvas(element).then(canvas => {
        let imgData = canvas.toDataURL("image/png");
        pdf.addImage(imgData, 'PNG', 10, 10, 180, 100);
        pdf.save(fileName + ".pdf");
    });
};
