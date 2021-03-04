// -- Initialize const and main items --

// var recipetDto, currency must be initialized at receipt.html

$(document).ready(function(){
    
    if(receiptDto!=undefined){
        for(item of receiptDto.items){
            addItem(null,item);
        }
    }
    calculateTotal();

});

const personStyles = new Map();
//key: style, value: isUsed
personStyles.set("btn btn-outline-primary", true);
personStyles.set("btn btn-outline-secondary", true);
personStyles.set("btn btn-outline-success", true);
personStyles.set("btn btn-outline-danger", false);
personStyles.set("btn btn-outline-warning", false);
personStyles.set("btn btn-outline-info", false);
personStyles.set("btn btn-outline-dark", false);

var itemList = document.getElementById('items');
var personList = document.getElementById('persons');
var addItemBtn = document.getElementById('add-new-item');
var liAddBtn = document.getElementById('li-add-button');
var addPersonBtn = document.getElementById('add-person');
var deletePersonBtn = document.getElementById('delete-person');


// -- Set action events --
addItemBtn.onclick = addItem;
addPersonBtn.onclick = addPerson;
deletePersonBtn.onclick = deletePerson;
itemList.addEventListener('click', processItem);
$(".price").change(priceOnChange);
$(".quantity").change(quantityOnChange);
$(".sum").change(sumOnChange);

// -- Persons section --

function getTotalForPerson(personId) {
    //get all rows where person got smthng
    var total = 0;
    var personInputs = document.querySelectorAll("input[name=" + personId + "]");
    for (count of personInputs) {
        var liNode = count.parentNode;
        var price = liNode.querySelector(".price");
        total += count.value * Number(price.value);
    }
    return total;
}

function generatePersonId() {
    return personList.children.length + 1;
}

function getFreeStyle() {
    for (var [key, used] of personStyles) {
        if (!used) {
            return key;
        }
    }
}

function useStyle(style){
    personStyles.set(style,true);
}

function setStyleNotUsed(style){
    personStyles.set(style,false);
}

function addPerson() {
    if (personList.children.length > personStyles.size-1) {
        return;
    }
    var freeStyle = getFreeStyle();
    var personLabel = document.createElement('label');
    personLabel.className = freeStyle;
    personLabel.style = "font-size:large";
    let personId = generatePersonId();
    personLabel.textContent = "Person " + personId;
    var radioBtn = document.createElement('input');
    radioBtn.type = "radio";
    radioBtn.name = "Person "+personId;
    radioBtn.id = "p-" + personId;
    radioBtn.autocomplete = "off";
    personLabel.appendChild(radioBtn);
    personList.appendChild(personLabel);
    personList.appendChild(document.createTextNode(" "));
    useStyle(freeStyle);
    $(personLabel).button('toggle');
}

function deletePerson() {

    if (personList.children.length < 2) {
        return;
    }
    personLabel = getCurrentPersonLabel();
    var personId = personLabel.children[0].id;
    var previous = personLabel.previousElementSibling;
    var toggleLabel = previous!=undefined?previous:personLabel.nextElementSibling;
    //delete all counts for the person
    allCounts = document.querySelectorAll("input[name=" + personId + "]");
    for(count of allCounts){
        var parentNode = count.parentNode;
        count.remove();
        refreshRowForFinished(parentNode);
    }
    $(toggleLabel).button('toggle');
    
    //free style
    //personLabel.classList.remove('active')
    var styleToFree = personLabel.className;
    setStyleNotUsed(styleToFree);

    //delete person
    personLabel.remove();
}

function getActivePersonId() {
    for (child of personList.children) {
        if (child.classList.contains('active')) {
            var toggledButton = child;
        }
    }
    return toggledButton.children[0].id;
}


function refreshTotalForPerson(personLabel) {
    input = personLabel.children[0];
    var total = getTotalForPerson(input.id);
    var totalText = (total > 0) ? " - " + total + " "+currency : "";
    personLabel.innerText = input.name + totalText;
    personLabel.appendChild(input);
}

function getCurrentPersonLabel() {
    for (person of personList.children) {
        if (person.classList.contains('active')) {
            return person;
        }
    }
}

// -- Items section --

function priceOnChange() {
    var quantityInput = this.parentNode.querySelector(".quantity");
    var sumInput = this.parentNode.querySelector(".sum");
    sumInput.value = Math.round(this.value * quantityInput.value * 100) / 100;
    calculateTotal();
}

function quantityOnChange() {
    var priceInput = this.parentNode.querySelector(".price");
    var sumInput = this.parentNode.querySelector(".sum");

    if ((priceInput.value == undefined || priceInput.value == 0)
        && sumInput.value != undefined
    ) {
        priceInput.value = Math.round(sumInput.value / this.value * 100) / 100;
    } else {
        sumInput.value = Math.round(this.value * priceInput.value * 100) / 100;
        calculateTotal();
    }
}

function sumOnChange() {
    var priceInput = this.parentNode.querySelector(".price");
    var quantityInput = this.parentNode.querySelector(".quantity");
    priceInput.value = Math.round(this.value / quantityInput.value * 100) / 100;

    calculateTotal();
}


function addItem(event,itemDto) {

    var li = document.createElement('li');
    li.className = 'list-group-item';

    var inputItem = document.createElement('input');
    inputItem.type = "text";
    inputItem.className = "item";
    inputItem.placeholder = "Item";
    li.appendChild(inputItem);
    li.appendChild(document.createTextNode(" "));

    var inputPrice = document.createElement('input');
    inputPrice.type = "number";
    inputPrice.className = "price";
    inputPrice.placeholder = "Price";
    inputPrice.addEventListener('change', priceOnChange);
    li.appendChild(inputPrice);
    li.appendChild(document.createTextNode(" "));

    var inputQuantity = document.createElement('input');
    inputQuantity.type = "number";
    inputQuantity.className = "quantity";
    inputQuantity.placeholder = "Quantity";
    inputQuantity.addEventListener('change', quantityOnChange);
    li.appendChild(inputQuantity);
    li.appendChild(document.createTextNode(" "));

    var inputSum = document.createElement('input');
    inputSum.type = "number";
    inputSum.className = "sum";
    inputSum.placeholder = "Sum";
    inputSum.addEventListener('change', sumOnChange);
    li.appendChild(inputSum);
    li.appendChild(document.createTextNode(" "));

    if(itemDto!=undefined){
        inputItem.value=itemDto.title;
        inputPrice.value = itemDto.price;
        inputQuantity.value = itemDto.quantity;
        inputSum.value = itemDto.sum;
    }

    //Button group
    var divBtnGroup = document.createElement('div');
    divBtnGroup.className = "btn-group mt-2 float-right";

    var btnDelete = document.createElement('button');
    btnDelete.className = "btn btn-lg btn-danger btn delete";
    btnDelete.textContent = "X";

    var btnPlus = document.createElement('button');
    btnPlus.className = "btn btn-lg btn-dark btn plus";
    btnPlus.textContent = "+";

    var btnMinus = document.createElement('button');
    btnMinus.className = "btn btn-lg btn-dark btn minus";
    btnMinus.textContent = "-";

    divBtnGroup.appendChild(btnMinus);
    divBtnGroup.appendChild(btnPlus);
    divBtnGroup.appendChild(btnDelete);

    li.appendChild(divBtnGroup);

    itemList.insertBefore(li, liAddBtn);
    return li;
}



function processItem(e) {

    var parentNode = e.target.parentNode.parentNode;
    var classList = e.target.classList;

    if (classList.contains('delete') && itemList.children.length > 4) {
        itemList.removeChild(parentNode);
        refreshAllTotals();
    } else {
        var currentPersonLabel = getCurrentPersonLabel();
        var countInput = getCountForPersonInputIfExist(parentNode);

        if (classList.contains('plus')) {
            if (countInput == null && !isRowFinished(parentNode)) {
                countInput = addCountForPersonInput(parentNode);
            }
            if (!isRowFinished(parentNode)) {
                countInput.value++;
            }
        } else if (classList.contains('minus')) {
            if (countInput != null && countInput.value > 0) {
                countInput.value--;
                if (countInput.value == 0) {
                    countInput.remove();
                }
            }
        }
        refreshTotalForPerson(currentPersonLabel);
    }
    refreshRowForFinished(parentNode);
}


function getCountForPersonInputIfExist(parentNode) {
    var currentPersonLabel = getCurrentPersonLabel();
    var personId = currentPersonLabel.children[0].id;
    return parentNode.querySelector("input[name=" + personId + "]");

}

function addCountForPersonInput(parentNode) {
    var currentPersonLabel = getCurrentPersonLabel();
    var personCountInput = document.createElement('input');
    personCountInput.classList = currentPersonLabel.classList;
    personCountInput.classList.remove('active');
    personCountInput.classList.add('countForPerson');
    personCountInput.classList.add('btn-lg');
    personCountInput.classList.add('font-weight-bold');
    personCountInput.classList.add('mr-1');
    personCountInput.name = currentPersonLabel.children[0].id;
    parentNode.appendChild(personCountInput);
    return personCountInput;
}


// -- Utility section --

function calculateTotal() {
    var total = 0;
    var totalHeader = document.getElementById('total');
    var sumArray = document.querySelectorAll(".sum");
    for (element of sumArray) {
        total += Number(element.value);
    }
    totalHeader.innerText = total + " "+currency;
}

function refreshAllTotals() {
    for (personLabel of personList.children) {
        refreshTotalForPerson(personLabel);
    }
    calculateTotal();
}

function refreshRowForFinished(liNode){
    if(isRowFinished(liNode)){
        liNode.classList.add('finished');
    } else{
        liNode.classList.remove('finished');
    }
}

function isRowFinished(liNode) {
    var countInputs = liNode.querySelectorAll(".countForPerson");
    var totalCount = 0;
    for (e of countInputs) {
        totalCount += Number(e.value);
    }
    var quantityInput = liNode.querySelector(".quantity");

    return (totalCount == quantityInput.value) ? true : false;
}


