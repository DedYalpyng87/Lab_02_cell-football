# UML-модель: «Клеточный футбол» (лабораторные 3–4)

**Репозиторий:** Lab_02_cell-football  
**Автор:** Гузаревич Роман Алексеевич, группа 250541  

**Требования:** [SRS.md](../SRS.md)  
**Правила игры:** [game-rules.md](../game-rules.md)  
**Глоссарий:** [glossary.md](glossary.md)  
**Потоки событий (Use Case):** [use-case-scenarios.md](use-case-scenarios.md)  
**Мокапы:** [mockups/](../../mockups/)

## Файлы в этой папке

| Файл | Назначение |
|------|------------|
| `cell-football-lr34.drawio` | Проект diagrams.net: диаграммы на отдельных страницах |
| `export/` | Экспорт диаграмм в PNG для GitHub и отчёта |
| `glossary.md` | Глоссарий предметной области |
| `use-case-scenarios.md` | Потоки событий (Flow of events) по вариантам использования с диаграммы `03-use-case` |

## Страницы в draw.io → экспорт

| Страница в .drawio | Файл PNG | Содержание |
|--------------------|----------|------------|
| `00-title` | `export/00-title.png` | Титул: проект, ФИО, группа, ЛР 3–4 |
| `02-domain-class` | `export/02-domain-class.png` | Доменная диаграмма классов |
| `03-use-case` | `export/03-use-case.png` | Диаграмма вариантов использования |
| `04-activity-human-move`| `export/04-activity-human-move.png`| Деятельность: ход человека |
| `05-activity-ai-move` | `export/05-activity-ai-move.png` | Деятельность: ход ИИ |

После экспорта из diagrams.net проверь имя файла (иногда получается `*.drawio.png` — переименуй в имя из таблицы).

## Как открыть

Открой [diagrams.net](https://app.diagrams.net) → **Файл → Открыть из** → выбери `cell-football-lr34.drawio` в этой папке. Страницы переключаются **вкладками внизу** окна.

---

*Дальше по ЛР 3–4: activity, sequence, state, уточнённая классовая, component, deployment — новые страницы в `.drawio` и строки в таблице выше + PNG в `export/`.*