package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

public class Tet extends AnimatorCard
{
    public static final String ID = Register(Tet.class);

    public Tet()
    {
        super(ID, 0, CardRarity.UNCOMMON, CardType.SKILL, CardTarget.NONE);

        Initialize(0, 0);

        SetInnate(true);
        SetRetain(true);
        SetExhaust(true);

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        ShuffleFromDiscardPile();
        DiscardFromDrawPile();

        if (HasSynergy() && EffectHistory.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.GainEnergy(1);
        }
    }

    private void DiscardFromDrawPile()
    {
        GameActions.Top.SelectFromPile(name, magicNumber, player.drawPile)
        .SetMessage(GR.Common.Strings.GridSelection.DiscardUpTo(magicNumber))
        .SetOptions(false, true)
        .AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                GameActions.Top.MoveCard(card, player.discardPile);
            }
        });
    }

    private void ShuffleFromDiscardPile()
    {
        GameActions.Top.SelectFromPile(name, magicNumber, player.discardPile)
        .SetMessage(GR.Common.Strings.GridSelection.MoveToDrawPile(magicNumber))
        .SetOptions(false, true)
        .AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                GameActions.Top.MoveCard(card, player.drawPile);
            }
        });
    }
}