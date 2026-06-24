import WidgetKit
import SwiftUI
import ComposeApp

struct Provider: TimelineProvider {
    private var isUk: Bool {
        Locale.current.language.languageCode?.identifier == "uk"
    }

    func placeholder(in context: Context) -> SimpleEntry {
        SimpleEntry(
            date: Date(),
            pageNumber: 1,
            excerpt: isUk ? "Завантаження..." : "Loading...",
            isPlaceholder: true
        )
    }

    func getSnapshot(in context: Context, completion: @escaping (SimpleEntry) -> ()) {
        WidgetBridge.shared.getWidgetData { data in
            let entry = SimpleEntry(
                date: Date(),
                pageNumber: data != nil ? Int(data!.pageNumber) : 1,
                excerpt: data?.excerpt ?? (isUk ? "Продовжити читання" : "Continue Reading"),
                isPlaceholder: data == nil
            )
            completion(entry)
        }
    }

    func getTimeline(in context: Context, completion: @escaping (Timeline<Entry>) -> ()) {
        WidgetBridge.shared.getWidgetData { data in
            let entry = SimpleEntry(
                date: Date(),
                pageNumber: data != nil ? Int(data!.pageNumber) : 1,
                excerpt: data?.excerpt ?? (isUk ? "Продовжити читання" : "Continue Reading"),
                isPlaceholder: data == nil
            )
            let timeline = Timeline(entries: [entry], policy: .atEnd)
            completion(timeline)
        }
    }
}

struct SimpleEntry: TimelineEntry {
    let date: Date
    let pageNumber: Int
    let excerpt: String
    let isPlaceholder: Bool
}

struct ContinueReadingWidgetEntryView: View {
    var entry: Provider.Entry

    // Localized strings map
    private var appName: String {
        let isUk = Locale.current.language.languageCode?.identifier == "uk"
        return isUk ? "Мала Книжка" : "Mala Knyzhka"
    }

    private var pageLabel: String {
        let isUk = Locale.current.language.languageCode?.identifier == "uk"
        return isUk ? "Сторінка \(entry.pageNumber)" : "Page \(entry.pageNumber)"
    }

    private var continueReading: String {
        let isUk = Locale.current.language.languageCode?.identifier == "uk"
        return isUk ? "Продовжити читання" : "Continue Reading"
    }

    private var openApp: String {
        let isUk = Locale.current.language.languageCode?.identifier == "uk"
        return isUk ? "Відкрийте додаток" : "Open the app to start"
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(appName)
                .font(.caption)
                .fontWeight(.bold)
                .foregroundColor(.accentColor)

            if entry.isPlaceholder {
                Text(continueReading)
                    .font(.headline)
                Text(openApp)
                    .font(.subheadline)
                    .foregroundColor(.secondary)
            } else {
                Text(pageLabel)
                    .font(.headline)

                Text(entry.excerpt)
                    .font(.subheadline)
                    .lineLimit(3)
                    .foregroundColor(.secondary)
            }
            Spacer()
        }
        .containerBackground(.clear, for: .widget)
        .widgetURL(URL(string: "malaknyzhka://page/\(entry.pageNumber)"))
    }
}

struct ContinueReadingWidget: Widget {
    let kind: String = "ContinueReadingWidget"

    private var displayName: String {
        let isUk = Locale.current.language.languageCode?.identifier == "uk"
        return isUk ? "Продовжити читання" : "Continue Reading"
    }

    private var descriptionText: String {
        let isUk = Locale.current.language.languageCode?.identifier == "uk"
        return isUk ? "Швидко повертайтеся до останньої відкритої сторінки." : "Quickly return to your last opened page."
    }

    var body: some WidgetConfiguration {
        StaticConfiguration(kind: kind, provider: Provider()) { entry in
            ContinueReadingWidgetEntryView(entry: entry)
        }
        .configurationDisplayName(displayName)
        .description(descriptionText)
        .supportedFamilies([.systemSmall, .systemMedium])
    }
}
