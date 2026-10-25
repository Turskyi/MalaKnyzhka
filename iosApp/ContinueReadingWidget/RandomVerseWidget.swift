import WidgetKit
import SwiftUI
import ComposeApp

struct RandomVerseProvider: TimelineProvider {
    private var isUk: Bool {
        Locale.current.language.languageCode?.identifier == "uk"
    }

    func placeholder(in context: Context) -> RandomVerseEntry {
        RandomVerseEntry(
            date: Date(),
            pageNumber: 1,
            snippet: isUk ? "Реве та стогне Дніпр широкий..." : "The wide Dnieper roars and groans...",
            isPlaceholder: true
        )
    }

    func getSnapshot(in context: Context, completion: @escaping (RandomVerseEntry) -> ()) {
        WidgetBridge.shared.getRandomVerseData { data in
            let entry = RandomVerseEntry(
                date: Date(),
                pageNumber: data != nil ? Int(data!.pageNumber) : 1,
                snippet: data?.snippet ?? (isUk ? "Реве та стогне Дніпр широкий..." : "The wide Dnieper roars and groans..."),
                isPlaceholder: data == nil
            )
            completion(entry)
        }
    }

    func getTimeline(in context: Context, completion: @escaping (Timeline<RandomVerseEntry>) -> ()) {
        WidgetBridge.shared.getRandomVerseData { data in
            let entry = RandomVerseEntry(
                date: Date(),
                pageNumber: data != nil ? Int(data!.pageNumber) : 1,
                snippet: data?.snippet ?? (isUk ? "Реве та стогне Дніпр широкий..." : "The wide Dnieper roars and groans..."),
                isPlaceholder: data == nil
            )
            let timeline = Timeline(entries: [entry], policy: .atEnd)
            completion(timeline)
        }
    }
}

struct RandomVerseEntry: TimelineEntry {
    let date: Date
    let pageNumber: Int
    let snippet: String
    let isPlaceholder: Bool
}

struct RandomVerseWidgetEntryView: View {
    @Environment(\.widgetFamily) var family
    var entry: RandomVerseProvider.Entry

    private var isUk: Bool {
        Locale.current.language.languageCode?.identifier == "uk"
    }

    private var pageLabel: String {
        isUk ? "Сторінка \(entry.pageNumber)" : "Page \(entry.pageNumber)"
    }

    private var maxLines: Int {
        switch family {
        case .systemSmall:
            return 4
        case .systemMedium:
            return 6
        case .systemLarge:
            return 15
        default:
            return 6
        }
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(isUk ? "Випадковий вірш" : "Random Verse")
                .font(.system(size: 13, weight: .bold))
                .foregroundColor(Color(hex: "3D2C1E"))

            Rectangle()
                .fill(Color(hex: "5C4735"))
                .frame(height: 1)

            Spacer(minLength: 2)

            Text("“ \(entry.snippet)")
                .font(.system(size: 13, weight: .regular, design: .serif))
                .italic()
                .foregroundColor(Color(hex: "3D2C1E"))
                .lineLimit(maxLines)

            Spacer()

            Text(pageLabel)
                .font(.system(size: 11, weight: .medium))
                .foregroundColor(Color(hex: "5C4735"))
        }
        .padding(12)
        .containerBackground(for: .widget) {
            Color(hex: "EFE9DF")
        }
        .widgetURL(URL(string: "malaknyzhka://page/\(entry.pageNumber)"))
    }
}

struct RandomVerseWidget: Widget {
    let kind: String = "RandomVerseWidget"

    private var isUk: Bool {
        Locale.current.language.languageCode?.identifier == "uk"
    }

    var body: some WidgetConfiguration {
        StaticConfiguration(kind: kind, provider: RandomVerseProvider()) { entry in
            RandomVerseWidgetEntryView(entry: entry)
        }
        .configurationDisplayName(isUk ? "Випадковий вірш" : "Random Verse")
        .description(isUk ? "Показує випадковий вірш з Малої Книжки на кожен день." : "Shows a random verse from Mala Knyzhka every day.")
        .supportedFamilies([.systemSmall, .systemMedium, .systemLarge])
    }
}
