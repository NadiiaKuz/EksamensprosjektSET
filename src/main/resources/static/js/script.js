const ham = document.getElementById('ham')

const closeM = document.getElementById('close')

const menu = document.getElementById('menu')

ham.addEventListener('click', ()=>{
    event.preventDefault();                 //Sjekk om denne kan fjernes senere
    menu.classList.toggle('hidden');
})

closeM.addEventListener('click', ()=>{
    event.preventDefault();                 //Sjekk om denne kan fjernes senere
    menu.classList.toggle('hidden')
})