package ru.sorokin.flyfilmsx.model

import java.util.*
import kotlin.collections.ArrayList

class Repository : IRepository {
    override fun getFilmsFromServer() = ArrayList<Film>()

    override fun getFilmsFromLocalStorage(): List<Film> {
        var films = ArrayList<Film>()
        films.add(Film(0
            , "Лука"
            , "Свои незабываемые каникулы, в которых есть место и домашней пасте, и мороженному, и бесконечным поездкам на мопеде, мальчик по имени Лука проводит в красивом приморском городке, расположенном на итальянской Ривьере. Ни одно приключение Луки не обходится без участия его нового лучшего друга, и беззаботность отдыха омрачает только лишь тот факт, что на самом деле в облике мальчика скрывается морской монстр из глубин лагуны, на берегу которой стоит городок."
            , Date()
            , "мультфильм, комедия, семейный, фэнтези"
            , "luca"
            , 9.6f))
        films.add(Film(1
            , "Локи"
            , "Продолжение приключений хитроумного бога обмана Локи, который будет появляться на Земле, влияя на ход её истории. Сериал расскажет о том, что произошло с ним после событий фильма «Мстители: Финал»."
            , Date()
            , "драма, НФ и Фэнтези"
            , "loki"
            , 8.7f))
        films.add(Film(2
            , "Гладко на бумаге"
            , "Андреа Сингер годами ставила свою стендап карьеру выше романтических отношений. И вот она знакомится с парнем, который кажется слишком идеальным, чтобы быть настоящим."
            , Date()
            , "комедия, мелодрама, триллер, детектив, драма"
            , "bumaga"
            , 7.4f))
        films.add(Film(3
            , "Спирит Непокорный"
            , "Лаки Прескотт — юная бунтарка, совсем как ее мама, легендарная бесстрашная наездница, которую дочь почти не помнит. После очередной шалости заботливая тетушка Кора, вырастившая девочку, отправляет ее жить к отцу. Теперь все, о чем мечтает Лаки — вырваться на волю из крошечного сонного городка. Все меняется, когда она знакомится со Спиритом — диким мустангом, таким же упрямым и независимым, как и она сама. После того как Спирит попадает в руки бессердечного ковбоя и его подельников, Лаки в сопровождении новых друзей отправляется в полное опасностей путешествие, чтобы его спасти."
            , Date()
            , "мультфильм, приключения, семейный, вестерн"
            , "kon"
            , 2.1f))
        films.add(Film(4
            , "Отцовство"
            , "Мэтт, чья жена умерла во время родов, оказывается один с маленькой дочкой на руках. Он не был готов стать отцом, но вынужден взять себя в руки и начать заботиться о ребёнке."
            , Date()
            , "драма, семейный, комедия"
            , "batya"
            , 10.0f))
        return films
    }
}