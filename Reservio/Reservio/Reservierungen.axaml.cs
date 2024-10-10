using Avalonia.Controls;
using Avalonia.Interactivity;

namespace Reservio{

public partial class Reservierungen : UserControl
{
    private string _kellnerName;
    public Reservierungen(string kellnerName)
    {
        InitializeComponent();
        _kellnerName = kellnerName;
    }
    private void Reservierung_Click(object? sender, RoutedEventArgs e)
    {
        var neueReservierungenWindow = new NeueReservierungen(_kellnerName);
        // Zeige die neue Seite

        this.Content = neueReservierungenWindow;
    }
}
}