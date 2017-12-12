def books = [
        [name: '1 Nephi', chapters: 22],
        [name: '2 Nephi', chapters: 33],
        [name: 'Jacob', chapters: 7],
        [name: 'Enos', chapters: 1],
        [name: 'Jarom', chapters: 1],
        [name: 'Omni', chapters: 1],
        [name: 'Words of Mormon', chapters: 1],
        [name: 'Mosiah', chapters: 29],
        [name: 'Alma', chapters: 63],
        [name: 'Helaman', chapters: 16],
        [name: '3 Nephi', chapters: 30],
        [name: '4 Nephi', chapters: 1],
        [name: 'Mormon', chapters: 9],
        [name: 'Ether', chapters: 15],
        [name: 'Moroni', chapters: 10]
]

def assignments = []

def generateAssignments = { int dailyCount ->
    def currentBook = null
    def currentCount = 0;
    def dailyAssignment = ''

    books.each { book ->
        (1..book.chapters).each { chapter ->
            dailyAssignment += dailyAssignment ? ',' : ''
            dailyAssignment += "${currentBook != book.name ? ((dailyAssignment ? ' ' : '') + book.name) : ''} ${chapter}"
            currentCount++
            if (currentCount == dailyCount) {
                currentCount = 0
                assignments += dailyAssignment
                dailyAssignment = ''
                currentBook = null
            } else {
                currentBook = book.name
            }
        }
    }
    if (dailyAssignment) {
        assignments += dailyAssignment
    }
}

generateAssignments(2)

def file = new File('BoMCal.ics')
println file.path
def beginDate = new Date().parse('yyy/MM/dd', '2017/08/13')
def endDate = beginDate + 1
file << 'BEGIN:VCALENDAR\n'
assignments.each { assignment ->
    def event = """BEGIN:VEVENT
DTSTART;VALUE=DATE:${beginDate.format('yyyMMdd')}
DTEND;VALUE=DATE:${endDate.format('yyyMMdd')}
STATUS:CONFIRMED
SUMMARY:${assignment}
TRANSP:TRANSPARENT
END:VEVENT
"""
    file << event
    beginDate++
    endDate++
}
file << 'END:VCALENDAR\n'